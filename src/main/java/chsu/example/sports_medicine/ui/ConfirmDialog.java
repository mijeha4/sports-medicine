package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog {
    
    public ConfirmDialog(String title, String message, 
                        String confirmText, String cancelText,
                        Runnable confirmAction) {
        
        setModal(true);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
        
        H2 titleLabel = new H2(title);
        Paragraph messageLabel = new Paragraph(message);
        
        Button confirmButton = new Button(confirmText, event -> {
            confirmAction.run();
            close();
        });
        confirmButton.getStyle().set("color", "var(--lumo-error-color)");
        
        Button cancelButton = new Button(cancelText, event -> close());
        
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonsLayout.setSpacing(true);
        
        VerticalLayout layout = new VerticalLayout(titleLabel, messageLabel, buttonsLayout);
        layout.setSpacing(true);
        layout.setPadding(true);
        
        add(layout);
    }
}