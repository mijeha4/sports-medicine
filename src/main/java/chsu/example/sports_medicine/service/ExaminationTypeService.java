package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.ExaminationType;
import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.repository.ExaminationTypeRepository;
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
public class ExaminationTypeService {

    private final ExaminationTypeRepository examinationTypeRepository;

    @Autowired
    private MedicalExaminationRepository medicalExaminationRepository;

    @Autowired
    private PhysioIndicatorRepository physioIndicatorRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    public ExaminationTypeService(ExaminationTypeRepository examinationTypeRepository) {
        this.examinationTypeRepository = examinationTypeRepository;
    }

    public List<ExaminationType> findAll() {
        return examinationTypeRepository.findAll();
    }

    public ExaminationType findById(Long id) {
        return examinationTypeRepository.findById(id).orElse(null);
    }

    public ExaminationType save(ExaminationType examination) {
        return examinationTypeRepository.save(examination);
    }

    public void deleteById(Long id) {
        examinationTypeRepository.deleteById(id);
    }

    public Map<String, Long> getExaminationTypeDependencies(Long typeId) {
        Map<String, Long> dependencies = new HashMap<>();

        long examinationCount = medicalExaminationRepository.countByExaminationTypeId(typeId);
        dependencies.put("Медицинские осмотры", examinationCount);

        // Для каждого осмотра подсчитать показатели и рекомендации
        List<MedicalExamination> examinations = medicalExaminationRepository.findAll();
        long physioCount = 0;
        long recommendationCount = 0;
        for (MedicalExamination exam : examinations) {
            if (exam.getExaminationType() != null && exam.getExaminationType().getTypeId().equals(typeId)) {
                physioCount += physioIndicatorRepository.countByExaminationId(exam.getId());
                recommendationCount += recommendationRepository.countByExaminationId(exam.getId());
            }
        }
        dependencies.put("Физио показатели", physioCount);
        dependencies.put("Рекомендации", recommendationCount);

        return dependencies;
    }

    @Transactional
    public void cascadeDeleteExaminationType(Long typeId) {
        // Найти все осмотры типа
        List<MedicalExamination> examinations = medicalExaminationRepository.findAll();
        for (MedicalExamination exam : examinations) {
            if (exam.getExaminationType() != null && exam.getExaminationType().getTypeId().equals(typeId)) {
                // Удалить связанные показатели и рекомендации
                physioIndicatorRepository.deleteByExaminationId(exam.getId());
                recommendationRepository.deleteByExaminationId(exam.getId());
                // Удалить осмотр
                medicalExaminationRepository.delete(exam);
            }
        }
        // Удалить тип
        examinationTypeRepository.deleteById(typeId);
    }
}
