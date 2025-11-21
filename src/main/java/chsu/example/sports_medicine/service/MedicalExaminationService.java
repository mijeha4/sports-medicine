package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.repository.MedicalExaminationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalExaminationService {

    private final MedicalExaminationRepository medicalExaminationRepository;

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
}
