package chsu.example.sports_medicine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "medicalexaminations")
public class MedicalExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_id")
    private Long id; // Уникальный идентификатор медицинского осмотра

    // Отношения
    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete; // Спортсмен, связанный с этим осмотром
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // Врач, который провел осмотр
    @ManyToOne
    @JoinColumn(name = "type_id")
    private ExaminationType examinationType; // Тип осмотра

    // Детали осмотра
    @Column(name = "examination_date")
    private LocalDate date; // Дата осмотра
    @Column(name = "next_examination_date")
    private LocalDate next_date; // Дата осмотра
    @Column(name = "conclusion")
    private String conclusion; // Результаты осмотра

    public Long getExaminationId() {
        return id;
    }

    // Default constructor (REQUIRED for Binder!)
    public MedicalExamination() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public ExaminationType getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(ExaminationType examinationType) {
        this.examinationType = examinationType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getResults() {
        return conclusion;
    }

    public void setResults(String conclusion) {
        this.conclusion = conclusion;
    }
}
