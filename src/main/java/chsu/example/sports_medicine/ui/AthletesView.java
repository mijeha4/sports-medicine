package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "athletes", layout = MainLayout.class)
public class AthletesView extends VerticalLayout {

    private final AthleteService athleteService;
    private final Grid<Athlete> grid = new Grid<>(Athlete.class);

    @Autowired
    public AthletesView(AthleteService athleteService) {
        Div card = new Div();
        card.addClassName("card");
        card.addClassName("card--primary");

        this.athleteService = athleteService;
        addClassName("athletes-view");
        setSizeFull();

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        H2 title = new H2("Управление атлетами");
        title.addClassName("card-title");
        headerSection.add(title);
        Paragraph content = new Paragraph("Здесь вы можете просматривать, добавлять и управлять информацией о спортсменах, включая их личные данные, спортивные достижения и медицинские показатели.");
        content.addClassName("card-content");
        headerSection.add(content);
        //headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете просматривать, добавлять и управлять информацией о спортсменах, включая их личные данные, спортивные достижения и медицинские показатели."));

        configureGrid();
        add(headerSection, card, getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("athletes-grid");
        grid.setSizeFull();
        grid.setColumns("id", "first_name", "last_name", "date_of_birth", "sport_type", "phone", "registration_date");
        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("first_name").setHeader("Имя");
        grid.getColumnByKey("last_name").setHeader("Фамилия");
        grid.getColumnByKey("date_of_birth").setHeader("Дата рождения");
        grid.getColumnByKey("sport_type").setHeader("Тип спорта");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.getColumnByKey("registration_date").setHeader("Дата регистрации");
    }

    private VerticalLayout getToolbar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Поиск по имени или фамилии");
        searchField.addValueChangeListener(event -> {
            String query = event.getValue();
            if (query.isEmpty()) {
                updateList();
            } else {
                grid.setItems(athleteService.searchAthletes(query));
            }
        });

        Button addAthleteButton = new Button("Добавить атлета", click -> {
            AddAthleteDialog dialog = new AddAthleteDialog(athleteService);
            dialog.open();
        });
        addAthleteButton.addClassName("transition-all");
        addAthleteButton.getElement().setAttribute("theme", "primary");
        Button deleteAthleteButton = new Button("Удалить атлета", click -> {
            Athlete selectedAthlete = grid.asSingleSelect().getValue();
            if (selectedAthlete != null) {
                deleteAthlete(selectedAthlete);
            } else {
                Notification.show("Выберите атлета для удаления");
            }
        });
        return new VerticalLayout(searchField, addAthleteButton, deleteAthleteButton);
    }

    private void updateList() {
        grid.setItems(athleteService.findAll());
    }

    private void deleteAthlete(Athlete athlete) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("Удаление атлета");

        Paragraph message = new Paragraph(
            "Вы уверены, что хотите удалить атлета " +
            athlete.getFirstName() + " " + athlete.getLastName() + "?"
        );

        Button confirmButton = new Button("Удалить", event -> {
            try {
                athleteService.deleteAthlete(athlete.getId());
                updateList();
                Notification.show("Атлет удален");
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
