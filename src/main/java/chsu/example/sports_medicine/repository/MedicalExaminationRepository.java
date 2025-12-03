package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.MedicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {

    long countByAthleteId(Long athleteId);

    @Query("SELECT COUNT(me) FROM MedicalExamination me WHERE me.athlete.id = :athleteId")
    long countExaminationsByAthleteId(@Param("athleteId") Long athleteId);

    long countByDoctorId(Long doctorId);

    long countByExaminationTypeId(Long typeId);
}
