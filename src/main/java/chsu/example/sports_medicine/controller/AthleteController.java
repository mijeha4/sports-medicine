package chsu.example.sports_medicine.controller;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Athlete")
public class AthleteController {

    @Autowired
    private AthleteService athleteService;

    @GetMapping
    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    @PostMapping
    public Athlete createAthlete(@RequestBody Athlete athlete) {
        return athleteService.saveAthlete(athlete);
    }

    @DeleteMapping("/{id}")
    public void deleteAthlete(@PathVariable Long id) {
        athleteService.deleteAthlete(id);
    }
}
