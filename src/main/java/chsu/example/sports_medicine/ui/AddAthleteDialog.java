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
        this(athleteService, null, null);
    }

    public AddAthleteDialog(AthleteService athleteService, Athlete athlete) {
        this(athleteService, athlete, null);
    }

    public AddAthleteDialog(AthleteService athleteService, Athlete athlete, Runnable onSaveCallback) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        DatePicker ageField = new DatePicker("Дата рождения");
        TextField sportType = new TextField("Тип спорта");
        TextField phone = new TextField("Телефон");
        DatePicker regField = new DatePicker("Дата регистрации");

        formLayout.add(nameField, surnameField, ageField, sportType, phone, regField);

        binder.forField(nameField).bind(Athlete::getFirstName, Athlete::setFirstName);
        binder.forField(surnameField).bind(Athlete::getLastName, Athlete::setLastName);
        binder.forField(ageField).bind(Athlete::getDateOfBirth, Athlete::setDate_of_birth);
        binder.forField(sportType).bind(Athlete::getSport_type, Athlete::setSport_type);
        binder.forField(phone).bind(Athlete::getPhone, Athlete::setPhone);
        binder.forField(regField).bind(Athlete::getRegistration_date, Athlete::setRegistration_date);

        if (athlete != null) {
            binder.readBean(athlete);
        }

        Button saveButton = new Button("Сохранить", event -> {
            if (binder.validate().isOk()) {
                Athlete athleteToSave = athlete != null ? athlete : new Athlete();
                binder.writeBeanIfValid(athleteToSave);
                athleteService.saveAthlete(athleteToSave);
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
