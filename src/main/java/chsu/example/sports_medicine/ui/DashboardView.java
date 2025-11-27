package chsu.example.sports_medicine.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Pre;

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

    private Div createHeartRateStats() {
        Div statsDiv = new Div();
        statsDiv.addClassName("stats-card");

        List<PhysioIndicator> indicators = physioIndicatorService.findAll();
        double avgHeartRate = indicators.stream()
                .mapToDouble(PhysioIndicator::getMeasuredValue)
                .average()
                .orElse(0.0);

        statsDiv.add(new H2("Статистика ЧСС"));
        statsDiv.add(new Span("Средняя ЧСС: " + String.format("%.1f", avgHeartRate)));
        statsDiv.add(new Span("Всего измерений: " + indicators.size()));

        return statsDiv;
    }

    private Div createSportDistributionStats() {
        Div statsDiv = new Div();
        statsDiv.addClassName("stats-card");

        Map<String, Long> sportDistribution = athleteService.findAll().stream()
                .collect(Collectors.groupingBy(Athlete::getSport_type, Collectors.counting()));

        statsDiv.add(new H2("Распределение по видам спорта"));
        Pre distributionText = new Pre();
        StringBuilder sb = new StringBuilder();
        sportDistribution.forEach((sport, count) ->
            sb.append(sport).append(": ").append(count).append("\n"));
        distributionText.setText(sb.toString());
        statsDiv.add(distributionText);

        return statsDiv;
    }

    private Div createHealthStatusStats() {
        Div statsDiv = new Div();
        statsDiv.addClassName("stats-card");

        Map<String, Long> healthStatus = medicalExaminationService.findAll().stream()
                .collect(Collectors.groupingBy(MedicalExamination::getConclusion, Collectors.counting()));

        statsDiv.add(new H2("Состояние здоровья"));
        Pre statusText = new Pre();
        StringBuilder sb = new StringBuilder();
        healthStatus.forEach((status, count) ->
            sb.append(status).append(": ").append(count).append("\n"));
        statusText.setText(sb.toString());
        statsDiv.add(statusText);

        return statsDiv;
    }

   
}
