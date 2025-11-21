package chsu.example.sports_medicine.repository;

import chsu.example.sports_medicine.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    @Query("SELECT a FROM Athlete a WHERE " +
           "LOWER(a.first_name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.last_name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.sport_type) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Athlete> search(@Param("filter") String filter);
}
