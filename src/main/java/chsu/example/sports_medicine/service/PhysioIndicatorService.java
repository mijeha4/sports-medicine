package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.PhysioIndicator;
import chsu.example.sports_medicine.repository.PhysioIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysioIndicatorService {

    private final PhysioIndicatorRepository physioIndicatorRepository;

    @Autowired
    public PhysioIndicatorService(PhysioIndicatorRepository physioIndicatorRepository) {
        this.physioIndicatorRepository = physioIndicatorRepository;
    }

    public List<PhysioIndicator> findAll() {
        return physioIndicatorRepository.findAll();
    }

    public List<PhysioIndicator> findByIndicatorId(Long indicatorId) {
        return physioIndicatorRepository.findByIndicatorId(indicatorId);
    }

    public PhysioIndicator findById(Long id) {
        return physioIndicatorRepository.findById(id).orElse(null);
    }

    public PhysioIndicator save(PhysioIndicator physioIndicator) {
        return physioIndicatorRepository.save(physioIndicator);
    }

    public void deleteById(Long id) {
        physioIndicatorRepository.deleteById(id);
    }
}
