package chsu.example.sports_medicine.ui;

import chsu.example.sports_medicine.model.ExaminationType;
import chsu.example.sports_medicine.service.ExaminationTypeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;


public class AddExaminationDialog extends Dialog {

    private final Binder<ExaminationType> binder = new Binder<>(ExaminationType.class);

    public AddExaminationDialog(ExaminationTypeService examinationTypeService) {
        this(examinationTypeService, null);
    }

    public AddExaminationDialog(ExaminationTypeService examinationTypeService, ExaminationType examinationType) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Type Name");
        TextField descriptionField = new TextField("Description");

        formLayout.add(nameField, descriptionField);

        binder.forField(nameField).bind(ExaminationType::getTypeName, ExaminationType::setTypeName);
        binder.forField(descriptionField).bind(ExaminationType::getDescription, ExaminationType::setDescription);

        if (examinationType != null) {
            binder.readBean(examinationType);
        }

        Button saveButton = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                ExaminationType examinationToSave = examinationType != null ? examinationType : new ExaminationType();
                binder.writeBeanIfValid(examinationToSave);
                examinationTypeService.save(examinationToSave);
                close();
            }
        });

        Button cancelButton = new Button("Cancel", event -> close());

        VerticalLayout buttonLayout = new VerticalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        add(formLayout, buttonLayout);
    }
}
