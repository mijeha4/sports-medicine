package chsu.example.sports_medicine.ui;

import chsu.example.sports_medicine.model.Recommendation;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.RecommendationService;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.combobox.ComboBox;
import chsu.example.sports_medicine.model.MedicalExamination;

public class AddRecommendationDialog extends Dialog {

    FormLayout formLayout = new FormLayout();
    ComboBox<MedicalExamination> examinationField = new ComboBox<>("Examination");
    TextField recommendationTextField = new TextField("Recommendation Text");
    TextField priorityField = new TextField("Priority");
    TextField statusField = new TextField("Status");

    private final Binder<Recommendation> binder = new Binder<>(Recommendation.class);
    @Autowired
    public AddRecommendationDialog(RecommendationService recommendationService, MedicalExaminationService medicalExaminationService) {
        this(recommendationService, medicalExaminationService, null);
    }

    public AddRecommendationDialog(RecommendationService recommendationService, MedicalExaminationService medicalExaminationService, Recommendation recommendation) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeaderTitle(recommendation == null ? "Добавить рекомендацию" : "Изменить рекомендацию");

        examinationField.setItems(medicalExaminationService.findAll());
        examinationField.setItemLabelGenerator(medicalExamination -> medicalExamination.getId().toString());

        formLayout.add(examinationField, recommendationTextField, priorityField, statusField);

        binder.forField(examinationField).bind(Recommendation::getExamination, Recommendation::setExamination);
        binder.forField(recommendationTextField).bind(Recommendation::getRecommendationText, Recommendation::setRecommendationText);
        binder.forField(priorityField).bind(Recommendation::getPriority, Recommendation::setPriority);
        binder.forField(statusField).bind(Recommendation::getStatus, Recommendation::setStatus);

        if (recommendation != null) {
            binder.readBean(recommendation);
        }

        Button saveButton = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                Recommendation recommendationToSave = recommendation != null ? recommendation : new Recommendation();
                binder.writeBeanIfValid(recommendationToSave);
                recommendationService.save(recommendationToSave);
                close();
            }
        });

        Button cancelButton = new Button("Cancel", event -> close());

        VerticalLayout buttonLayout = new VerticalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        add(formLayout, buttonLayout);
    }
}
