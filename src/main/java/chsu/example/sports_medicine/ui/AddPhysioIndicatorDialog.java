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

    private final ComboBox<MedicalExamination> examinationField = new ComboBox<>("Осмотр");
    private final TextField indicatorNameField = new TextField("Имя физиотер. показателя");
    private final NumberField measuredValueField = new NumberField("измеренное значение");
    private final TextField unitField = new TextField("Ед. изм.");
    private final NumberField normalMinField = new NumberField("Нормальный минимум");
    private final NumberField normalMaxField = new NumberField("Нормальный максимум");

    @Autowired
    public AddPhysioIndicatorDialog(PhysioIndicatorService physioIndicatorService,
                                    MedicalExaminationService medicalExaminationService) {
        this(physioIndicatorService, medicalExaminationService, null, null);
    }

    public AddPhysioIndicatorDialog(PhysioIndicatorService physioIndicatorService,
                                    MedicalExaminationService medicalExaminationService,
                                    PhysioIndicator physioIndicator) {
        this(physioIndicatorService, medicalExaminationService, physioIndicator, null);
    }

    public AddPhysioIndicatorDialog(PhysioIndicatorService physioIndicatorService,
                                    MedicalExaminationService medicalExaminationService,
                                    PhysioIndicator physioIndicator,
                                    Runnable onSaveCallback) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeaderTitle(physioIndicator == null ? "Добавить физиотерапевтический показатель" : "Изменить физиотерапевтический показатель");
        setModal(true);

        FormLayout formLayout = new FormLayout();

        examinationField.setItems(medicalExaminationService.findAll());
        examinationField.setItemLabelGenerator(medicalExamination -> medicalExamination.getId().toString());

        formLayout.add(examinationField, indicatorNameField, measuredValueField, unitField, normalMinField, normalMaxField);

        binder.forField(examinationField).bind(PhysioIndicator::getExamination, PhysioIndicator::setExamination);
        binder.forField(indicatorNameField).bind(PhysioIndicator::getIndicatorName, PhysioIndicator::setIndicatorName);
        binder.forField(measuredValueField).bind(PhysioIndicator::getMeasuredValue, PhysioIndicator::setMeasuredValue);
        binder.forField(unitField).bind(PhysioIndicator::getUnit, PhysioIndicator::setUnit);
        binder.forField(normalMinField).bind(PhysioIndicator::getNormalMin, PhysioIndicator::setNormalMin);
        binder.forField(normalMaxField).bind(PhysioIndicator::getNormalMax, PhysioIndicator::setNormalMax);

        if (physioIndicator != null) {
            binder.readBean(physioIndicator);
        }

        Button saveButton = new Button("Сохранить", event -> {
            if (binder.validate().isOk()) {
                PhysioIndicator indicatorToSave = physioIndicator != null ? physioIndicator : new PhysioIndicator();
                binder.writeBeanIfValid(indicatorToSave);
                physioIndicatorService.save(indicatorToSave);
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
