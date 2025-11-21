package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;

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

@Route("dashboard")
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

        add(createHeader());
        add(createKpiSection());
        add(createChartsSection());
    }

    private H1 createHeader() {
        return new H1("Медицинский аналитический дэшборд");
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

        chartsLayout.add(createHeartRateChart());
        chartsLayout.add(createSportDistributionChart());
        chartsLayout.add(createHealthStatusChart());

        return chartsLayout;
    }

    private Chart createHeartRateChart() {
        Chart chart = new Chart(ChartType.LINE);
        chart.getConfiguration().setTitle("Динамика ЧСС по спортсменам");

        List<PhysioIndicator> indicators = physioIndicatorService.findAll();
        ListSeries series = new ListSeries("ЧСС");
        indicators.forEach(indicator -> series.addData(indicator.getMeasuredValue()));

        chart.getConfiguration().addSeries(series);

        return chart;
    }

    private Chart createSportDistributionChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.getConfiguration().setTitle("Распределение по видам спорта");

        Map<String, Long> sportDistribution = athleteService.findAll().stream()
                .collect(Collectors.groupingBy(Athlete::getSport_type, Collectors.counting()));

        DataSeries series = new DataSeries();
        sportDistribution.forEach((sport, count) ->
            series.add(new DataSeriesItem(sport, count.intValue())));

        chart.getConfiguration().addSeries(series);

        return chart;
    }

    private Chart createHealthStatusChart() {
        Chart chart = new Chart(ChartType.COLUMN);
        chart.getConfiguration().setTitle("Состояние здоровья");

        Map<String, Long> healthStatus = medicalExaminationService.findAll().stream()
                .collect(Collectors.groupingBy(MedicalExamination::getConclusion, Collectors.counting()));

        DataSeries series = new DataSeries();
        healthStatus.forEach((status, count) ->
            series.add(new DataSeriesItem(status, count.intValue())));

        chart.getConfiguration().addSeries(series);

        return chart;
    }

   
}