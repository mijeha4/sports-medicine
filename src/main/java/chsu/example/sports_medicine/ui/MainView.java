package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(true);

        RouterLink athletesLink = new RouterLink("Атлеты", AthletesView.class);
        RouterLink doctorsLink = new RouterLink("Доктора", DoctorsView.class);
        RouterLink examinationsLink = new RouterLink("Тип проверки", ExaminationsView.class);
        RouterLink medicalexaminationsLink = new RouterLink("Мед. осмотр", MedicalExaminationsView.class);
        RouterLink physioIndicatorsLink = new RouterLink("Физиотерапевтические показатели", PhysioIndicatorsView.class);
        RouterLink recommendationsLink = new RouterLink("Рекомендации", RecommendationsView.class);
        RouterLink dashboardLink = new RouterLink("Дэшборд", DashboardView.class);

        add(athletesLink, doctorsLink, examinationsLink, medicalexaminationsLink, physioIndicatorsLink, recommendationsLink, dashboardLink);
    }
}
