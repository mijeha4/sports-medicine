package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.Recommendation;
import chsu.example.sports_medicine.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public List<Recommendation> findAll() {
        return recommendationRepository.findAll();
    }

    public List<Recommendation> findByRecommendationId(Long recommendationId) {
        return recommendationRepository.findByRecommendationId(recommendationId);
    }

    public Recommendation findById(Long id) {
        return recommendationRepository.findById(id).orElse(null);
    }

    public Recommendation save(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    public void deleteById(Long id) {
        recommendationRepository.deleteById(id);
    }
}
