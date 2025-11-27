package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.model.PhysioIndicator;
import chsu.example.sports_medicine.service.AthleteService;
import chsu.example.sports_medicine.service.DoctorService;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import chsu.example.sports_medicine.service.RecommendationService;
import chsu.example.sports_medicine.service.PhysioIndicatorService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "dashboard", layout = MainLayout.class)
@CssImport("styles/dashboard-styles.css")
public class DashboardView extends VerticalLayout {

    private final AthleteService athleteService;
    private final MedicalExaminationService medicalExaminationService;
    private final RecommendationService recommendationService;
    private final PhysioIndicatorService physioIndicatorService;

    @Autowired
    public DashboardView(AthleteService athleteService,
                        DoctorService doctorService,
                        MedicalExaminationService medicalExaminationService,
                        RecommendationService recommendationService,
                        PhysioIndicatorService physioIndicatorService) {
        this.athleteService = athleteService;
        this.medicalExaminationService = medicalExaminationService;
        this.recommendationService = recommendationService;
        this.physioIndicatorService = physioIndicatorService;

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

        chartsLayout.add(createHeartRateStats());
        chartsLayout.add(createSportDistributionStats());
        chartsLayout.add(createHealthStatusStats());

        return chartsLayout;
    }

    private Chart createHeartRateStats() {
        Chart chart = new Chart(ChartType.LINE);
        chart.addClassName("stats-card");

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Статистика ЧСС");

        List<PhysioIndicator> indicators = physioIndicatorService.findAll();

        DataSeries series = new DataSeries("ЧСС");
        for (int i = 0; i < indicators.size(); i++) {
            PhysioIndicator indicator = indicators.get(i);
            DataSeriesItem item = new DataSeriesItem(i + 1, indicator.getMeasuredValue());
            series.add(item);
        }

        conf.addSeries(series);

        // No additional options needed for basic line chart

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

    private Chart createHealthStatusStats() {
        Chart chart = new Chart(ChartType.COLUMN);
        chart.addClassName("stats-card");

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Состояние здоровья");

        Map<String, Long> healthStatus = medicalExaminationService.findAll().stream()
                .collect(Collectors.groupingBy(MedicalExamination::getConclusion, Collectors.counting()));

        DataSeries series = new DataSeries("Количество");
        healthStatus.forEach((status, count) -> {
            DataSeriesItem item = new DataSeriesItem(status, count);
            series.add(item);
        });

        conf.addSeries(series);

        PlotOptionsColumn options = new PlotOptionsColumn();
        series.setPlotOptions(options);

        return chart;
    }

   
}
