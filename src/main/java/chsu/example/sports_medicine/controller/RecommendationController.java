package chsu.example.sports_medicine.controller;

import chsu.example.sports_medicine.model.Recommendation;
import chsu.example.sports_medicine.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public List<Recommendation> getAllRecommendations() {
        return recommendationService.findAll();
    }

    @GetMapping("/{id}")
    public Recommendation getRecommendationById(@PathVariable Long id) {
        return recommendationService.findById(id);
    }

    @PostMapping
    public Recommendation createRecommendation(@RequestBody Recommendation recommendation) {
        return recommendationService.save(recommendation);
    }

    @PutMapping("/{id}")
    public Recommendation updateRecommendation(@PathVariable Long id, @RequestBody Recommendation recommendation) {
        recommendation.setId(id);
        return recommendationService.save(recommendation);
    }

    @DeleteMapping("/{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteById(id);
    }
}
