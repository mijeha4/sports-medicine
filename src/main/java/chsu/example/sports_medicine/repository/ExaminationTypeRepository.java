package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationTypeRepository extends JpaRepository<ExaminationType, Long> {
    @Query("SELECT et FROM ExaminationType et WHERE et.typeId = :typeId")
    List<ExaminationType> findByTypeId(@Param("typeId") Long typeId);
}
