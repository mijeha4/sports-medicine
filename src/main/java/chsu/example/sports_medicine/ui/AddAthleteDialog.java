package chsu.example.sports_medicine.ui;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.service.AthleteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddAthleteDialog extends Dialog {

    private final Binder<Athlete> binder = new Binder<>(Athlete.class);
    @Autowired
    public AddAthleteDialog(AthleteService athleteService) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Name");
        TextField surnameField = new TextField("Surname");
        DatePicker ageField = new DatePicker("Date of Birth");
        TextField sportType = new TextField("Sport type");
        TextField phone = new TextField("Phone");
        DatePicker regField = new DatePicker("Registration Date");

        formLayout.add(nameField, surnameField, ageField, sportType, phone);

        binder.forField(nameField).bind(Athlete::getFirstName, Athlete::setFirstName);
        binder.forField(surnameField).bind(Athlete::getLastName, Athlete::setLastName);
        binder.forField(ageField).bind(Athlete::getDateOfBirth, Athlete::setDate_of_birth);
        binder.forField(sportType).bind(Athlete::getSport_type, Athlete::setSport_type);
        binder.forField(phone).bind(Athlete::getPhone, Athlete::setPhone);
        binder.forField(regField).bind(Athlete::getRegistration_date, Athlete::setRegistration_date);
            

        Button saveButton = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                Athlete athlete = new Athlete();
                binder.writeBeanIfValid(athlete);
                athleteService.saveAthlete(athlete);
                close();
            }
        });

        Button cancelButton = new Button("Cancel", event -> close());

        VerticalLayout buttonLayout = new VerticalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        add(formLayout, buttonLayout);
    }
}
