package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
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
        this.athleteService = athleteService;
        addClassName("athletes-view");
        setSizeFull();

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Управление атлетами"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете просматривать, добавлять и управлять информацией о спортсменах, включая их личные данные, спортивные достижения и медицинские показатели."));

        configureGrid();
        add(headerSection, getToolbar(), grid);
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
            AddAthleteDialog dialog = new AddAthleteDialog(athleteService, null, this::updateList);
            dialog.open();
        });
        Button editAthleteButton = new Button("Изменить атлета", click -> {
            Athlete selectedAthlete = grid.asSingleSelect().getValue();
            if (selectedAthlete != null) {
                AddAthleteDialog dialog = new AddAthleteDialog(athleteService, selectedAthlete, this::updateList);
                dialog.open();
            } else {
                Notification.show("Выберите атлета для изменения");
            }
        });
        Button deleteAthleteButton = new Button("Удалить атлета", click -> {
            Athlete selectedAthlete = grid.asSingleSelect().getValue();
            if (selectedAthlete != null) {
                deleteAthlete(selectedAthlete);
            } else {
                Notification.show("Выберите атлета для удаления");
            }
        });
        return new VerticalLayout(searchField, addAthleteButton, editAthleteButton, deleteAthleteButton);
    }

    private void updateList() {
        grid.setItems(athleteService.findAll());
    }

    private void deleteAthlete(Athlete athlete) {
        String displayName = athlete.getFirstName() + " " + athlete.getLastName();
        var dependencies = athleteService.getAthleteDependencies(athlete.getId());

        CascadeDeleteDialog dialog = new CascadeDeleteDialog("атлета", displayName, dependencies, () -> {
            try {
                athleteService.cascadeDeleteAthlete(athlete.getId());
                updateList();
                Notification.show("Атлет и связанные данные удалены");
            } catch (Exception e) {
                Notification.show("Ошибка при удалении: " + e.getMessage());
            }
        });
        dialog.open();
    }
}
