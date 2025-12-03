package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.Doctor;
import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.repository.DoctorRepository;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    private MedicalExaminationRepository medicalExaminationRepository;

    @Autowired
    private PhysioIndicatorRepository physioIndicatorRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctors(String query) {
        return doctorRepository.search(query);
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }

    public Map<String, Long> getDoctorDependencies(Long doctorId) {
        Map<String, Long> dependencies = new HashMap<>();

        long examinationCount = medicalExaminationRepository.countByDoctorId(doctorId);
        dependencies.put("Медицинские осмотры", examinationCount);

        // Для каждого осмотра подсчитать показатели и рекомендации
        List<MedicalExamination> examinations = medicalExaminationRepository.findAll();
        long physioCount = 0;
        long recommendationCount = 0;
        for (MedicalExamination exam : examinations) {
            if (exam.getDoctor() != null && exam.getDoctor().getDoctorId().equals(doctorId)) {
                physioCount += physioIndicatorRepository.countByExaminationId(exam.getId());
                recommendationCount += recommendationRepository.countByExaminationId(exam.getId());
            }
        }
        dependencies.put("Физио показатели", physioCount);
        dependencies.put("Рекомендации", recommendationCount);

        return dependencies;
    }

    @Transactional
    public void cascadeDeleteDoctor(Long doctorId) {
        // Найти все осмотры доктора
        List<MedicalExamination> examinations = medicalExaminationRepository.findAll();
        for (MedicalExamination exam : examinations) {
            if (exam.getDoctor() != null && exam.getDoctor().getDoctorId().equals(doctorId)) {
                // Удалить связанные показатели и рекомендации
                physioIndicatorRepository.deleteByExaminationId(exam.getId());
                recommendationRepository.deleteByExaminationId(exam.getId());
                // Удалить осмотр
                medicalExaminationRepository.delete(exam);
            }
        }
        // Удалить доктора
        doctorRepository.deleteById(doctorId);
    }
}
