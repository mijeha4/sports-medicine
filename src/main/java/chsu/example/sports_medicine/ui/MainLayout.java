package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Спортивная медицина")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
        addClassName("sports-medicine-app");
    }

    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Меню");

        H1 title = new H1("Спортивная медицина");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Avatar avatar = new Avatar("Пользователь");

        Header header = new Header(toggle, title, avatar);
        header.addClassNames(
            "bg-base", "border-b", "border-contrast-10",
            "box-border", "flex", "h-xl", "items-center", "justify-between", "w-full", "px-m"
        );

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav sideNav = new SideNav();

        sideNav.addItem(
            new SideNavItem("Дэшборд", DashboardView.class, VaadinIcon.DASHBOARD.create()),
            new SideNavItem("Атлеты", AthletesView.class, VaadinIcon.USERS.create()),
            new SideNavItem("Доктора", DoctorsView.class, VaadinIcon.DOCTOR.create()),
            new SideNavItem("Тип проверки", ExaminationsView.class, VaadinIcon.CHECK_SQUARE_O.create()),
            new SideNavItem("Мед. осмотр", MedicalExaminationsView.class, VaadinIcon.HEART.create()),
            new SideNavItem("Физио показатели", PhysioIndicatorsView.class, VaadinIcon.CHART.create()),
            new SideNavItem("Рекомендации", RecommendationsView.class, VaadinIcon.LIGHTBULB.create())
        );

        // Просто VerticalLayout с отступами — в Vaadin 24 этого достаточно
        var drawerContent = new com.vaadin.flow.component.orderedlayout.VerticalLayout(sideNav);
        drawerContent.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Gap.MEDIUM,
            "flex", "flex-col", "h-full"
        );

        addToDrawer(drawerContent);
    }
}
