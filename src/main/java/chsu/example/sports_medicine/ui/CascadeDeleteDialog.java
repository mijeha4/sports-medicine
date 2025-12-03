package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Map;

public class CascadeDeleteDialog extends Dialog {

    public CascadeDeleteDialog(String entityName, String entityDisplayName, Map<String, Long> dependencies, Runnable confirmAction) {
        setModal(true);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);

        H2 title = new H2("Внимание! Удаление " + entityName.toLowerCase() + " " + entityDisplayName + " приведет к удалению следующих связанных данных:");
        title.getStyle().set("color", "var(--lumo-error-color)");

        VerticalLayout dependenciesLayout = new VerticalLayout();
        dependenciesLayout.setSpacing(false);
        dependenciesLayout.setPadding(false);

        if (dependencies.isEmpty() || dependencies.values().stream().allMatch(count -> count == 0)) {
            // Нет зависимостей, простой диалог
            Paragraph noDepsMessage = new Paragraph("Вы уверены, что хотите удалить " + entityName.toLowerCase() + " " + entityDisplayName + "?");
            add(noDepsMessage);
        } else {
            // Есть зависимости, показать список
            for (Map.Entry<String, Long> entry : dependencies.entrySet()) {
                if (entry.getValue() > 0) {
                    HorizontalLayout depRow = new HorizontalLayout();
                    depRow.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
                    depRow.setSpacing(true);

                    Span icon = new Span(VaadinIcon.WARNING.create());
                    icon.getStyle().set("color", "var(--lumo-error-color)");

                    Span text = new Span(entry.getKey() + ": " + entry.getValue());
                    text.addClassNames(LumoUtility.FontWeight.BOLD);

                    depRow.add(icon, text);
                    dependenciesLayout.add(depRow);
                }
            }
            add(title, dependenciesLayout);
        }

        Button confirmButton = new Button("Удалить", event -> {
            confirmAction.run();
            close();
        });
        confirmButton.getStyle().set("color", "var(--lumo-error-color)");
        confirmButton.addClassNames(LumoUtility.Background.ERROR_10);

        Button cancelButton = new Button("Отмена", event -> close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonsLayout.setJustifyContentMode(com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.END);
        buttonsLayout.setSpacing(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.add(buttonsLayout);

        add(layout);
    }
}
