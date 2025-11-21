package chsu.example.sports_medicine.controller;

import chsu.example.sports_medicine.model.MedicalExamination;
import chsu.example.sports_medicine.service.MedicalExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-examinations")
public class MedicalExaminationController {

    private final MedicalExaminationService medicalExaminationService;

    @Autowired
    public MedicalExaminationController(MedicalExaminationService medicalExaminationService) {
        this.medicalExaminationService = medicalExaminationService;
    }

    @GetMapping
    public List<MedicalExamination> getAllMedicalExaminations() {
        return medicalExaminationService.findAll();
    }

    @GetMapping("/{id}")
    public MedicalExamination getMedicalExaminationById(@PathVariable Long id) {
        return medicalExaminationService.findById(id);
    }

    @PostMapping
    public MedicalExamination createMedicalExamination(@RequestBody MedicalExamination medicalExamination) {
        return medicalExaminationService.save(medicalExamination);
    }

    @PutMapping("/{id}")
    public MedicalExamination updateMedicalExamination(@PathVariable Long id, @RequestBody MedicalExamination medicalExamination) {
        medicalExamination.setId(id);
        return medicalExaminationService.save(medicalExamination);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicalExamination(@PathVariable Long id) {
        medicalExaminationService.deleteById(id);
    }
}
