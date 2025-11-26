package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;

@Route(value = "", layout = MainLayout.class)  // ← ВАЖНО!
public class MainView extends VerticalLayout {

    public MainView() {
        addClassName("main-content");
        setPadding(true);
        setSpacing(true);

        add(new H2("Добро пожаловать в систему спортивной медицины"));
        add(new Paragraph("Выберите раздел в боковом меню слева"));
    }
}
