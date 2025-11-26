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

import chsu.example.sports_medicine.model.ExaminationType;

import chsu.example.sports_medicine.service.ExaminationTypeService;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "examinations", layout = MainLayout.class)
public class ExaminationsView extends VerticalLayout {

    private final ExaminationTypeService examinationTypeService;
    private final Grid<ExaminationType> grid = new Grid<>(ExaminationType.class);

    @Autowired
    public ExaminationsView(ExaminationTypeService examinationTypeService) {
        this.examinationTypeService = examinationTypeService;
        addClassName("examinations-view");
        setSizeFull();

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Управление типами обследований"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете просматривать, добавлять и управлять различными типами медицинских обследований, включая их описания и параметры."));

        configureGrid();
        add(headerSection, getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("examinations-grid");
        grid.setSizeFull();
        grid.setColumns("typeId", "typeName", "description");
        grid.getColumnByKey("typeId").setHeader("ID");
        grid.getColumnByKey("typeName").setHeader("Название");
        grid.getColumnByKey("description").setHeader("Описание");
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
                    Long id = Long.parseLong(query);
                    grid.setItems(examinationTypeService.findById(id));
                } catch (NumberFormatException e) {
                    Notification.show("Введите корректный indicatorId");
                }
            }
        });

        Button addExaminationButton = new Button("Добавить проверку", click -> {
            AddExaminationDialog dialog = new AddExaminationDialog(examinationTypeService);
            dialog.open();
        });
        Button deleteExaminationTypeButton = new Button("Удалить тип", click -> {
            ExaminationType selectedExaminationType = grid.asSingleSelect().getValue();
            if (selectedExaminationType != null) {
                deleteExaminationType(selectedExaminationType);
            } else {
                Notification.show("Выберите тип для удаления");
            }
        });
        return new VerticalLayout(searchField, addExaminationButton, deleteExaminationTypeButton);
    }

    private void updateList() {
        grid.setItems(examinationTypeService.findAll());
    }
    private void deleteExaminationType(ExaminationType examinationType) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("Удаление типа");
        
        Paragraph message = new Paragraph(
            "Вы уверены, что хотите удалить тип " + 
            examinationType.getTypeName() + "?"
        );
        
        Button confirmButton = new Button("Удалить", event -> {
            try {
                examinationTypeService.deleteById(examinationType.getTypeId());
                updateList();
                Notification.show("Тип удален");
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
