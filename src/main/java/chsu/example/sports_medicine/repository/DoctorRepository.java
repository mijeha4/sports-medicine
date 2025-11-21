package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d WHERE " +
           "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Doctor> search(@Param("filter") String filter);
}
