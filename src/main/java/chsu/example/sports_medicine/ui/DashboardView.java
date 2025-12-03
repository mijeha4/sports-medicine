package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.service.AthleteService;
import chsu.example.sports_medicine.service.DoctorService;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.RecommendationService;
import chsu.example.sports_medicine.service.PhysioIndicatorService;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "dashboard", layout = MainLayout.class)
@CssImport("styles/dashboard-styles.css")
public class DashboardView extends VerticalLayout {

    private final AthleteService athleteService;
    private final RecommendationService recommendationService;

    @Autowired
    public DashboardView(AthleteService athleteService,
                        DoctorService doctorService,
                        MedicalExaminationService medicalExaminationService,
                        RecommendationService recommendationService,
                        PhysioIndicatorService physioIndicatorService) {
        this.athleteService = athleteService;
        this.recommendationService = recommendationService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);
        addClassName("dashboard-view");

        // Add header and description
        VerticalLayout headerSection = new VerticalLayout();
        headerSection.addClassName("header-section");
        headerSection.add(new com.vaadin.flow.component.html.H1("Медицинский аналитический дэшборд"));
        headerSection.add(new com.vaadin.flow.component.html.Paragraph("Здесь вы можете видеть ключевые показатели здоровья спортсменов, аналитику обследований и рекомендации для улучшения спортивных результатов."));

        add(headerSection, createKpiSection(), createChartsSection());
    }


    private HorizontalLayout createKpiSection() {
        HorizontalLayout kpiLayout = new HorizontalLayout();
        kpiLayout.setWidthFull();
        kpiLayout.setSpacing(true);

        kpiLayout.add(createKpiCard("Всего спортсменов", String.valueOf(athleteService.findAll().size())));
        kpiLayout.add(createKpiCard("Обследования за месяц", "15")); // Placeholder
        kpiLayout.add(createKpiCard("Отклонения от нормы", "3")); // Placeholder
        kpiLayout.add(createKpiCard("Активные рекомендации", String.valueOf(recommendationService.findAll().size())));

        return kpiLayout;
    }

    private Div createKpiCard(String title, String value) {
        Div card = new Div();
        card.addClassName("kpi-card");

        H2 titleElement = new H2(title);
        Span valueElement = new Span(value);

        card.add(titleElement, valueElement);
        return card;
    }

    private HorizontalLayout createChartsSection() {
        HorizontalLayout chartsLayout = new HorizontalLayout();
        chartsLayout.setWidthFull();
        chartsLayout.setSpacing(true);

        chartsLayout.add(createAgeDistributionStats());
        chartsLayout.add(createSportDistributionStats());

        return chartsLayout;
    }

        private Chart createAgeDistributionStats() {
            Chart chart = new Chart(ChartType.COLUMN);
            chart.addClassName("stats-card");

            Configuration conf = chart.getConfiguration();
            conf.setTitle("Возрастное распределение спортсменов");

            XAxis xAxis = new XAxis();
        xAxis.setType(AxisType.CATEGORY); // Говорим графику брать подписи из имен DataSeriesItem
        xAxis.setTitle("Возрастные группы"); // Необязательно: название самой оси
        conf.addxAxis(xAxis);

        List<Athlete> athletes = athleteService.findAll();

        Map<String, Long> ageGroups = athletes.stream()
                .filter(athlete -> athlete.getDateOfBirth() != null)
                .map(athlete -> {
                    int age = LocalDate.now().getYear() - athlete.getDateOfBirth().getYear();
                    if (age < 20) return "10-19";
                    else if (age < 30) return "20-29";
                    else if (age < 40) return "30-39";
                    else return "40+";
                })
                .collect(Collectors.groupingBy(age -> age, Collectors.counting()));

        DataSeries series = new DataSeries("Количество");
        ageGroups.forEach((ageGroup, count) -> {
            DataSeriesItem item = new DataSeriesItem(ageGroup, count);
            series.add(item);
        });

        List<String> orderedKeys = List.of("10-19", "20-29", "30-39", "40+");
        for (String key : orderedKeys) {
        // Если в какой-то группе никого нет, ставим 0, иначе берем значение из map
        Long count = ageGroups.getOrDefault(key, 0L);
        
        // Первый параметр (key) станет подписью столбца, второй (count) - высотой
        DataSeriesItem item = new DataSeriesItem(key, count);
        
        // Можно раскрасить каждый столбец в свой цвет (по желанию)
        // item.setColor(DataSeriesItem.Color.parseColor("#HEXCODE")); 
        
        series.add(item);
        }

        conf.addSeries(series);

        PlotOptionsColumn options = new PlotOptionsColumn();
        options.getDataLabels().setEnabled(true);
        series.setPlotOptions(options);

        return chart;
    }

    private Chart createSportDistributionStats() {
        Chart chart = new Chart(ChartType.PIE);
        chart.addClassName("stats-card");

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Распределение по видам спорта");

        Map<String, Long> sportDistribution = athleteService.findAll().stream()
                .collect(Collectors.groupingBy(Athlete::getSport_type, Collectors.counting()));

        DataSeries series = new DataSeries();
        sportDistribution.forEach((sport, count) -> {
            DataSeriesItem item = new DataSeriesItem(sport, count);
            series.add(item);
        });

        conf.addSeries(series);

        PlotOptionsPie options = new PlotOptionsPie();
        options.setAllowPointSelect(true);
        options.setShowInLegend(true);
        series.setPlotOptions(options);

        return chart;
    }
}
