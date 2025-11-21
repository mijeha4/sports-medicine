package chsu.example.sports_medicine.ui;

import chsu.example.sports_medicine.model.PhysioIndicator;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.PhysioIndicatorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.combobox.ComboBox;
import chsu.example.sports_medicine.model.MedicalExamination;

import org.springframework.beans.factory.annotation.Autowired;

public class AddPhysioIndicatorDialog extends Dialog {

    private final Binder<PhysioIndicator> binder = new Binder<>(PhysioIndicator.class);

    private final ComboBox<MedicalExamination> examinationField = new ComboBox<>("Examination");
    private final TextField indicatorNameField = new TextField("Indicator Name");
    private final NumberField measuredValueField = new NumberField("Measured Value");
    private final TextField unitField = new TextField("Unit");
    private final NumberField normalMinField = new NumberField("Normal Min");
    private final NumberField normalMaxField = new NumberField("Normal Max");
    
    @Autowired
    public AddPhysioIndicatorDialog(PhysioIndicatorService physioIndicatorService,
                                    MedicalExaminationService medicalExaminationService) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeaderTitle("Добавить медицинский осмотр");
        setModal(true);

        FormLayout formLayout = new FormLayout();

        examinationField.setItems(medicalExaminationService.findAll());
        examinationField.setItemLabelGenerator(medicalExamination -> medicalExamination.getId().toString());

        formLayout.add(examinationField, indicatorNameField, measuredValueField, unitField, normalMinField, normalMaxField);

        Button saveButton = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                PhysioIndicator physioIndicator = new PhysioIndicator();
                binder.writeBeanIfValid(physioIndicator);
                physioIndicatorService.save(physioIndicator);
                close();
            }
        });

        Button cancelButton = new Button("Cancel", event -> close());

        VerticalLayout buttonLayout = new VerticalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        add(formLayout, buttonLayout);
    }
}
