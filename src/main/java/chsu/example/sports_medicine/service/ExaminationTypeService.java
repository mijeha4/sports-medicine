package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.ExaminationType;
import chsu.example.sports_medicine.repository.ExaminationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminationTypeService {

    private final ExaminationTypeRepository examinationTypeRepository;

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
}
