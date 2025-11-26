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

import chsu.example.sports_medicine.model.Doctor;
import chsu.example.sports_medicine.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "doctors", layout = MainLayout.class)
public class DoctorsView extends VerticalLayout {

    private final DoctorService doctorService;
    private final Grid<Doctor> grid = new Grid<>(Doctor.class);

    @Autowired
    public DoctorsView(DoctorService doctorService) {
        this.doctorService = doctorService;
        addClassName("doctors-view");
        setSizeFull();

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Управление врачами"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете просматривать, добавлять и управлять информацией о врачах, включая их специализацию, лицензионные данные и медицинские квалификации."));

        configureGrid();
        add(headerSection, getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("doctors-grid");
        grid.setSizeFull();
        grid.setColumns("doctorId", "firstName", "lastName", "specialization", "licenseNumber");
        grid.getColumnByKey("doctorId").setHeader("ID");
        grid.getColumnByKey("firstName").setHeader("Имя");
        grid.getColumnByKey("lastName").setHeader("Фамилия");
        grid.getColumnByKey("specialization").setHeader("Специализация");
        grid.getColumnByKey("licenseNumber").setHeader("Номер лицензии");
    }

    private VerticalLayout getToolbar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Поиск по имени или фамилии");
        searchField.addValueChangeListener(event -> {
            String query = event.getValue();
            if (query.isEmpty()) {
                updateList();
            } else {
                grid.setItems(doctorService.searchDoctors(query));
            }
        });

        Button addDoctorButton = new Button("Добавить доктора", click -> {
            AddDoctorDialog dialog = new AddDoctorDialog(doctorService);
            dialog.open();
        });
        Button deleteDoctorButton = new Button("Удалить доктора", click -> {
            Doctor selectedDoctor = grid.asSingleSelect().getValue();
            if (selectedDoctor != null) {
                deleteDoctor(selectedDoctor);
            } else {
                Notification.show("Выберите атлета для удаления");
            }
        });
        return new VerticalLayout(searchField, addDoctorButton, deleteDoctorButton);
    }

    private void updateList() {
        grid.setItems(doctorService.findAll());
    }

    private void deleteDoctor(Doctor doctor) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("Удаление доктора");

        Paragraph message = new Paragraph(
            "Вы уверены, что хотите удалить доктора " +
            doctor.getFirstName() + " " + doctor.getLastName() + "?"
        );

        Button confirmButton = new Button("Удалить", event -> {
            try {
                doctorService.deleteById(doctor.getDoctorId());
                updateList();
                Notification.show("Атлет удален");
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
