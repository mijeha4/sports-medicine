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

import chsu.example.sports_medicine.model.PhysioIndicator;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.PhysioIndicatorService;

import org.springframework.beans.factory.annotation.Autowired;

@Route("physio-indicators")
public class PhysioIndicatorsView extends VerticalLayout {

    private final PhysioIndicatorService physioIndicatorService;
    private final MedicalExaminationService medicalExaminationService;
    private final Grid<PhysioIndicator> grid = new Grid<>(PhysioIndicator.class);

    @Autowired
    public PhysioIndicatorsView(PhysioIndicatorService physioIndicatorService, MedicalExaminationService medicalExaminationService) {
        this.physioIndicatorService = physioIndicatorService;
        this.medicalExaminationService = medicalExaminationService;
        addClassName("physio-indicators-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("physio-indicators-grid");
        grid.setSizeFull();
        grid.setColumns("indicatorId", "examination.id", "indicatorName", "measuredValue", "unit", "normalMin", "normalMax");
        grid.getColumnByKey("examination.id").setHeader("MedicalExamination Id");
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
                    Long indicatorId = Long.parseLong(query);
                    grid.setItems(physioIndicatorService.findByIndicatorId(indicatorId));
                } catch (NumberFormatException e) {
                    Notification.show("Введите корректный indicatorId");
                }
            }
        });

        Button addPhysioIndicatorButton = new Button("Добавить физиотерапевтический показатель", click -> {
            AddPhysioIndicatorDialog dialog = new AddPhysioIndicatorDialog(physioIndicatorService, medicalExaminationService);
            dialog.open();
        });
        Button deletePhysioIndicatorButton = new Button("Удалить физиотерапевтический показатель", click -> {
            PhysioIndicator selectedPhysioIndicator = grid.asSingleSelect().getValue();
            if (selectedPhysioIndicator != null) {
                deletePhysioIndicator(selectedPhysioIndicator);
            } else {
                Notification.show("Выберите физиотерапевтический показатель для удаления");
            }
        });
        return new VerticalLayout(searchField, addPhysioIndicatorButton, deletePhysioIndicatorButton);
    }

    private void updateList() {
        grid.setItems(physioIndicatorService.findAll());
    }
    private void deletePhysioIndicator(PhysioIndicator physioIndicator) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("Удаление физиотерапевтического показателя");

        Paragraph message = new Paragraph(
            "Вы уверены, что хотите удалить физиотерапевтический показатель " +
            physioIndicator.getIndicatorName() + "?"
        );

        Button confirmButton = new Button("Удалить", event -> {
            try {
                physioIndicatorService.deleteById(physioIndicator.getIndicatorId());
                updateList();
                Notification.show("Физиотерапевтический показатель удален");
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
