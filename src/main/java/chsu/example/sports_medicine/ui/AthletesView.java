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
import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("athletes")
public class AthletesView extends VerticalLayout {

    private final AthleteService athleteService;
    private final Grid<Athlete> grid = new Grid<>(Athlete.class);

    @Autowired
    public AthletesView(AthleteService athleteService) {
        this.athleteService = athleteService;
        addClassName("athletes-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("athletes-grid");
        grid.setSizeFull();
        grid.setColumns("id", "first_name", "last_name", "date_of_birth", "sport_type", "phone", "registration_date");
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
