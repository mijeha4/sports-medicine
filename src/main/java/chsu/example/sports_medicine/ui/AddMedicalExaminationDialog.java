package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.model.Doctor;
import chsu.example.sports_medicine.model.ExaminationType;
import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.service.AthleteService;
import chsu.example.sports_medicine.service.DoctorService;
import chsu.example.sports_medicine.service.ExaminationTypeService;
import chsu.example.sports_medicine.service.MedicalExaminationService;

import org.springframework.beans.factory.annotation.Autowired;

public class AddMedicalExaminationDialog extends Dialog {

    private final MedicalExaminationService medicalExaminationService;

    private final TextField conclusionField = new TextField("Заключение");
    private final DatePicker datePicker = new DatePicker("Дата осмотра");
    private final ComboBox<Athlete> athleteComboBox = new ComboBox<>("Спортсмен");
    private final ComboBox<Doctor> doctorComboBox = new ComboBox<>("Врач");
    private final ComboBox<ExaminationType> examinationTypeComboBox = new ComboBox<>("Тип осмотра");

    @Autowired
    public AddMedicalExaminationDialog(MedicalExaminationService medicalExaminationService,
                                      AthleteService athleteService,
                                      DoctorService doctorService,
                                      ExaminationTypeService examinationTypeService) {
        this.medicalExaminationService = medicalExaminationService;

        setHeaderTitle("Добавить медицинский осмотр");
        setModal(true);
        
        FormLayout formLayout = new FormLayout();
        athleteComboBox.setItems(athleteService.findAll());
        athleteComboBox.setItemLabelGenerator(athlete -> athlete.getFirstName() + " " + athlete.getLastName());

        doctorComboBox.setItems(doctorService.findAll());
        doctorComboBox.setItemLabelGenerator(doctor -> doctor.getFirstName() + " " + doctor.getLastName());

        examinationTypeComboBox.setItems(examinationTypeService.findAll());
        examinationTypeComboBox.setItemLabelGenerator(ExaminationType::getTypeName);

        formLayout.add(conclusionField, datePicker, athleteComboBox, doctorComboBox, examinationTypeComboBox);

        Button saveButton = new Button("Сохранить", event -> saveMedicalExamination());
        Button cancelButton = new Button("Отмена", event -> close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);

        add(formLayout, buttonsLayout);
    }

    private void saveMedicalExamination() {
        MedicalExamination medicalExamination = new MedicalExamination();
        medicalExamination.setAthlete(athleteComboBox.getValue());
        medicalExamination.setDoctor(doctorComboBox.getValue());
        medicalExamination.setExaminationType(examinationTypeComboBox.getValue());
        medicalExamination.setDate(datePicker.getValue());
        medicalExamination.setResults(conclusionField.getValue());

        try {
            medicalExaminationService.save(medicalExamination);
            Notification.show("Медицинский осмотр добавлен");
            close();
        } catch (Exception e) {
            Notification.show("Ошибка при добавлении: " + e.getMessage());
        }
    }
}
