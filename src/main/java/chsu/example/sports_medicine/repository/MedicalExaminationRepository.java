package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.MedicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {
}
