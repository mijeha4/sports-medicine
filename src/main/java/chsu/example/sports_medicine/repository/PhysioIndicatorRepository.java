package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.PhysioIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysioIndicatorRepository extends JpaRepository<PhysioIndicator, Long> {
    @Query("SELECT pi FROM PhysioIndicator pi WHERE pi.indicatorId = :indicatorId")
    List<PhysioIndicator> findByIndicatorId(@Param("indicatorId") Long indicatorId);
}
