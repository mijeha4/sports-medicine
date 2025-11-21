package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    @Query("SELECT r FROM Recommendation r WHERE r.recommendationId = :recommendationId")
    List<Recommendation> findByRecommendationId(@Param("recommendationId") Long recommendationId);
}
