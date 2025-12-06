package chsu.example.sports_medicine.ui;

import chsu.example.sports_medicine.model.Doctor;
import chsu.example.sports_medicine.service.DoctorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class AddDoctorDialog extends Dialog {

    private final Binder<Doctor> binder = new Binder<>(Doctor.class);

    public AddDoctorDialog(DoctorService doctorService) {
        this(doctorService, null, null);
    }

    public AddDoctorDialog(DoctorService doctorService, Doctor doctor) {
        this(doctorService, doctor, null);
    }

    public AddDoctorDialog(DoctorService doctorService, Doctor doctor, Runnable onSaveCallback) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        FormLayout formLayout = new FormLayout();
        TextField firstNameField = new TextField("Имя");
        TextField lastNameField = new TextField("Фамилия");
        TextField specializationField = new TextField("Специализация");
        TextField licenseNumberField = new TextField("Номер лицензии");

        formLayout.add(firstNameField, lastNameField, specializationField, licenseNumberField);

        binder.forField(firstNameField).bind(Doctor::getFirstName, Doctor::setFirstName);
        binder.forField(lastNameField).bind(Doctor::getLastName, Doctor::setLastName);
        binder.forField(specializationField).bind(Doctor::getSpecialization, Doctor::setSpecialization);
        binder.forField(licenseNumberField).bind(Doctor::getLicenseNumber, Doctor::setLicenseNumber);

        if (doctor != null) {
            binder.readBean(doctor);
        }

        Button saveButton = new Button("Сохранить", event -> {
            if (binder.validate().isOk()) {
                Doctor doctorToSave = doctor != null ? doctor : new Doctor();
                binder.writeBeanIfValid(doctorToSave);
                doctorService.save(doctorToSave);
                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
                close();
            }
        });

        Button cancelButton = new Button("Отмена", event -> close());

        VerticalLayout buttonLayout = new VerticalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        add(formLayout, buttonLayout);
    }
}
