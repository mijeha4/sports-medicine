package chsu.example.sports_medicine.controller;

import chsu.example.sports_medicine.model.ExaminationType;
import chsu.example.sports_medicine.service.ExaminationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examination-types")
public class ExaminationTypeController {

    private final ExaminationTypeService examinationTypeService;

    @Autowired
    public ExaminationTypeController(ExaminationTypeService examinationTypeService) {
        this.examinationTypeService = examinationTypeService;
    }

    @GetMapping
    public List<ExaminationType> getAllExaminationTypes() {
        return examinationTypeService.findAll();
    }

    @GetMapping("/{id}")
    public ExaminationType getExaminationTypeById(@PathVariable Long id) {
        return examinationTypeService.findById(id);
    }

    @PostMapping
    public ExaminationType createExaminationType(@RequestBody ExaminationType examinationType) {
        return examinationTypeService.save(examinationType);
    }

    @PutMapping("/{id}")
    public ExaminationType updateExaminationType(@PathVariable Long id, @RequestBody ExaminationType examinationType) {
        examinationType.setId(id);
        return examinationTypeService.save(examinationType);
    }

    @DeleteMapping("/{id}")
    public void deleteExaminationType(@PathVariable Long id) {
        examinationTypeService.deleteById(id);
    }
}
