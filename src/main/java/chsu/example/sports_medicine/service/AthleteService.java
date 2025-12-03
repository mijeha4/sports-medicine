package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.repository.AthleteRepository;
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
public class AthleteService {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private MedicalExaminationRepository medicalExaminationRepository;

    @Autowired
    private PhysioIndicatorRepository physioIndicatorRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    public List<Athlete> searchAthletes(String query) {
        return athleteRepository.search(query);
    }

    public Athlete saveAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void deleteAthlete(Long id) {
        athleteRepository.deleteById(id);
    }

    public List<Athlete> getAllAthletes() {
        return findAll();
    }

    public Map<String, Long> getAthleteDependencies(Long athleteId) {
        Map<String, Long> dependencies = new HashMap<>();

        long examinationCount = medicalExaminationRepository.countByAthleteId(athleteId);
        dependencies.put("Медицинские осмотры", examinationCount);

        // Для каждого осмотра подсчитать показатели и рекомендации
        List<MedicalExamination> examinations = medicalExaminationRepository.findAll(); // Можно оптимизировать, но для простоты
        long physioCount = 0;
        long recommendationCount = 0;
        for (MedicalExamination exam : examinations) {
            if (exam.getAthlete() != null && exam.getAthlete().getId().equals(athleteId)) {
                physioCount += physioIndicatorRepository.countByExaminationId(exam.getId());
                recommendationCount += recommendationRepository.countByExaminationId(exam.getId());
            }
        }
        dependencies.put("Физио показатели", physioCount);
        dependencies.put("Рекомендации", recommendationCount);

        return dependencies;
    }

    @Transactional
    public void cascadeDeleteAthlete(Long athleteId) {
        // Найти все осмотры атлета
        List<MedicalExamination> examinations = medicalExaminationRepository.findAll();
        for (MedicalExamination exam : examinations) {
            if (exam.getAthlete() != null && exam.getAthlete().getId().equals(athleteId)) {
                // Удалить связанные показатели и рекомендации
                physioIndicatorRepository.deleteByExaminationId(exam.getId());
                recommendationRepository.deleteByExaminationId(exam.getId());
                // Удалить осмотр
                medicalExaminationRepository.delete(exam);
            }
        }
        // Удалить атлета
        athleteRepository.deleteById(athleteId);
    }

    // Вспомогательные методы для репозиториев, если нужны
    public void deleteByExaminationId(Long examinationId) {
        physioIndicatorRepository.deleteByExaminationId(examinationId);
    }
}
