package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.AthleteService;
import chsu.example.sports_medicine.service.DoctorService;
import chsu.example.sports_medicine.service.ExaminationTypeService;

import org.springframework.beans.factory.annotation.Autowired;

@Route("medicalexaminations")
public class MedicalExaminationsView extends VerticalLayout {

    private final MedicalExaminationService medicalExaminationService;
    private final AthleteService athleteService;
    private final DoctorService doctorService;
    private final ExaminationTypeService examinationTypeService;
    private final Grid<MedicalExamination> grid = new Grid<>(MedicalExamination.class);

    @Autowired
    public MedicalExaminationsView(MedicalExaminationService medicalExaminationService,
                                   AthleteService athleteService,
                                   DoctorService doctorService,
                                   ExaminationTypeService examinationTypeService) {
        this.medicalExaminationService = medicalExaminationService;
        this.athleteService = athleteService;
        this.doctorService = doctorService;
        this.examinationTypeService = examinationTypeService;
        addClassName("medicalexaminations-view");
        setSizeFull();

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Управление медицинскими осмотрами"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете просматривать, добавлять и управлять медицинскими осмотрами спортсменов, включая даты, результаты и заключения врачей."));

        configureGrid();
        add(headerSection, getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("medicalexaminations-grid");
        grid.setSizeFull();
        grid.setColumns("id", "athlete.id", "doctor.doctorId", "examinationType.typeId", "date", "conclusion");
        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("athlete.id").setHeader("ID Атлета");
        grid.getColumnByKey("doctor.doctorId").setHeader("ID Доктора");
        grid.getColumnByKey("examinationType.typeId").setHeader("ID Типа Обследования");
        grid.getColumnByKey("date").setHeader("Дата");
        grid.getColumnByKey("conclusion").setHeader("Заключение");
    }

    private VerticalLayout getToolbar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Поиск по id");
        searchField.addValueChangeListener(event -> {
            String query = event.getValue();
            if (query.isEmpty()) {
                updateList();
            } else {
                try {
                    Long id = Long.parseLong(query);
                    grid.setItems(medicalExaminationService.findById(id));
                } catch (NumberFormatException e) {
                    Notification.show("Введите корректный id");
                }
            }
        });

        Button addMedicalExaminationButton = new Button("Добавить осмотр", click -> {
            AddMedicalExaminationDialog dialog = new AddMedicalExaminationDialog(
                medicalExaminationService,
                athleteService,
                doctorService,
                examinationTypeService
            );
            dialog.open();
        });
        Button deleteMedicalExaminationButton = new Button("Удалить осмотр", click -> {
            MedicalExamination selectedMedicalExamination = grid.asSingleSelect().getValue();
            if (selectedMedicalExamination != null) {
                deleteMedicalExamination(selectedMedicalExamination);
            } else {
                Notification.show("Выберите осмотр для удаления");
            }
        });
        return new VerticalLayout(searchField, addMedicalExaminationButton, deleteMedicalExaminationButton);
    }

    private void updateList() {
        grid.setItems(medicalExaminationService.findAll());
    }

    private void deleteMedicalExamination(MedicalExamination medicalExamination) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("Удаление осмотра");

        Paragraph message = new Paragraph(
            "Вы уверены, что хотите удалить осмотр с id " +
            medicalExamination.getId() + "?"
        );

        Button confirmButton = new Button("Удалить", event -> {
            try {
                medicalExaminationService.deleteById(medicalExamination.getId());
                updateList();
                Notification.show("Осмотр удален");
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Ошибка при удалении: " + e.getMessage());
            }
        });
        confirmButton.getStyle().set("color", "var(--lumo-error-color)");

        Button cancelButton = new Button("Отмена", event -> confirmDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout layout = new VerticalLayout(message, buttonsLayout);
        layout.setSpacing(true);
        layout.setPadding(false);

        confirmDialog.add(layout);
        confirmDialog.open();
    }
}
