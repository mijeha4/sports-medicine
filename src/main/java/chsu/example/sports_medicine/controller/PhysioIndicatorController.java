package chsu.example.sports_medicine.controller;

import chsu.example.sports_medicine.model.PhysioIndicator;
import chsu.example.sports_medicine.service.PhysioIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/physio-indicators")
public class PhysioIndicatorController {

    private final PhysioIndicatorService physioIndicatorService;

    @Autowired
    public PhysioIndicatorController(PhysioIndicatorService physioIndicatorService) {
        this.physioIndicatorService = physioIndicatorService;
    }

    @GetMapping
    public List<PhysioIndicator> getAllPhysioIndicators() {
        return physioIndicatorService.findAll();
    }

    @GetMapping("/{id}")
    public PhysioIndicator getPhysioIndicatorById(@PathVariable Long id) {
        return physioIndicatorService.findById(id);
    }

    @PostMapping
    public PhysioIndicator createPhysioIndicator(@RequestBody PhysioIndicator physioIndicator) {
        return physioIndicatorService.save(physioIndicator);
    }

    @PutMapping("/{id}")
    public PhysioIndicator updatePhysioIndicator(@PathVariable Long id, @RequestBody PhysioIndicator physioIndicator) {
        physioIndicator.setId(id);
        return physioIndicatorService.save(physioIndicator);
    }

    @DeleteMapping("/{id}")
    public void deletePhysioIndicator(@PathVariable Long id) {
        physioIndicatorService.deleteById(id);
    }
}
