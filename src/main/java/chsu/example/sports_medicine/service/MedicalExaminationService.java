package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.repository.MedicalExaminationRepository;
import chsu.example.sports_medicine.repository.PhysioIndicatorRepository;
import chsu.example.sports_medicine.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MedicalExaminationService {

    private final MedicalExaminationRepository medicalExaminationRepository;

    @Autowired
    private PhysioIndicatorRepository physioIndicatorRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    public MedicalExaminationService(MedicalExaminationRepository medicalExaminationRepository) {
        this.medicalExaminationRepository = medicalExaminationRepository;
    }

    public List<MedicalExamination> findAll() {
        return medicalExaminationRepository.findAll();
    }

    public MedicalExamination findById(Long id) {
        return medicalExaminationRepository.findById(id).orElse(null);
    }

    public MedicalExamination save(MedicalExamination medicalExamination) {
        return medicalExaminationRepository.save(medicalExamination);
    }

    public void deleteById(Long id) {
        medicalExaminationRepository.deleteById(id);
    }

    public Map<String, Long> getMedicalExaminationDependencies(Long examId) {
        Map<String, Long> dependencies = new HashMap<>();

        long physioCount = physioIndicatorRepository.countByExaminationId(examId);
        dependencies.put("Физио показатели", physioCount);

        long recommendationCount = recommendationRepository.countByExaminationId(examId);
        dependencies.put("Рекомендации", recommendationCount);

        return dependencies;
    }

    @Transactional
    public void cascadeDeleteMedicalExamination(Long examId) {
        // Удалить связанные показатели и рекомендации
        physioIndicatorRepository.deleteByExaminationId(examId);
        recommendationRepository.deleteByExaminationId(examId);
        // Удалить осмотр
        medicalExaminationRepository.deleteById(examId);
    }
}
