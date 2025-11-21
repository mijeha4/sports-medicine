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

import chsu.example.sports_medicine.model.Recommendation;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("recommendations")
public class RecommendationsView extends VerticalLayout {

    private final RecommendationService recommendationService;
    private final MedicalExaminationService medicalExaminationService;
    private final Grid<Recommendation> grid = new Grid<>(Recommendation.class);

    @Autowired
    public RecommendationsView(RecommendationService recommendationService, MedicalExaminationService medicalExaminationService) {
        this.recommendationService = recommendationService;
        this.medicalExaminationService = medicalExaminationService;
        addClassName("recommendations-view");
        setSizeFull();

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Управление рекомендациями"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете просматривать, добавлять и управлять медицинскими рекомендациями для спортсменов, включая их приоритеты и статус выполнения."));

        configureGrid();
        add(headerSection, getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("recommendations-grid");
        grid.setSizeFull();
        grid.setColumns("recommendationId", "examination.id", "recommendationText", "priority", "status");
        grid.getColumnByKey("recommendationId").setHeader("ID");
        grid.getColumnByKey("examination.id").setHeader("ID Мед. Осмотра");
        grid.getColumnByKey("recommendationText").setHeader("Текст Рекомендации");
        grid.getColumnByKey("priority").setHeader("Приоритет");
        grid.getColumnByKey("status").setHeader("Статус");
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
                    Long recommendationId = Long.parseLong(query);
                    grid.setItems(recommendationService.findByRecommendationId(recommendationId));
                } catch (NumberFormatException e) {
                    Notification.show("Введите корректный recommendationId");
                }
            }
        });

        Button addRecommendationButton = new Button("Добавить рекомендации", click -> {
            AddRecommendationDialog dialog = new AddRecommendationDialog(recommendationService, medicalExaminationService);
            dialog.open();
        });
        Button deleteRecommendationButton = new Button("Удалить рекомендацию", click -> {
            Recommendation selectedRecommendation = grid.asSingleSelect().getValue();
            if (selectedRecommendation != null) {
                deleteRecommendation(selectedRecommendation);
            } else {
                Notification.show("Выберите рекомендацию для удаления");
            }
        });
        return new VerticalLayout(searchField, addRecommendationButton, deleteRecommendationButton);
    }

    private void updateList() {
        grid.setItems(recommendationService.findAll());
    }

    private void deleteRecommendation(Recommendation recommendation) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("Удаление рекомендации");

        Paragraph message = new Paragraph(
            "Вы уверены, что хотите удалить рекомендацию " +
            recommendation.getRecommendationId() + "?"
        );

        Button confirmButton = new Button("Удалить", event -> {
            try {
                recommendationService.deleteById(recommendation.getRecommendationId());
                updateList();
                Notification.show("Рекомендация удален");
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
