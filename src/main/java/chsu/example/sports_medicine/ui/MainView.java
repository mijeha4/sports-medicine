package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.button.Button;

@Route("")
@CssImport("styles/mainview-styles.css")
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(true);
        addClassName("main-view");

        Button athletesButton = new Button("Атлеты");
        athletesButton.addClassName("navigation-button");
        athletesButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AthletesView.class)));

        Button doctorsButton = new Button("Доктора");
        doctorsButton.addClassName("navigation-button");
        doctorsButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(DoctorsView.class)));

        Button examinationsButton = new Button("Тип проверки");
        examinationsButton.addClassName("navigation-button");
        examinationsButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ExaminationsView.class)));

        Button medicalexaminationsButton = new Button("Мед. осмотр");
        medicalexaminationsButton.addClassName("navigation-button");
        medicalexaminationsButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(MedicalExaminationsView.class)));

        Button physioIndicatorsButton = new Button("Физиотерапевтические показатели");
        physioIndicatorsButton.addClassName("navigation-button");
        physioIndicatorsButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(PhysioIndicatorsView.class)));

        Button recommendationsButton = new Button("Рекомендации");
        recommendationsButton.addClassName("navigation-button");
        recommendationsButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(RecommendationsView.class)));

        Button dashboardButton = new Button("Дэшборд");
        dashboardButton.addClassName("navigation-button");
        dashboardButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(DashboardView.class)));

        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Спортивная медицина"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Управление спортсменами, врачами и медицинскими обследованиями"));

        VerticalLayout navigationContainer = new VerticalLayout();
        navigationContainer.addClassName("navigation-container");
        navigationContainer.add(athletesButton, doctorsButton, examinationsButton, medicalexaminationsButton, physioIndicatorsButton, recommendationsButton, dashboardButton);

        add(headerSection, navigationContainer);
    }
}
