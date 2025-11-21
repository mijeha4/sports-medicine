package chsu.example.sports_medicine.service;

import chsu.example.sports_medicine.model.Athlete;
import chsu.example.sports_medicine.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AthleteService {

    @Autowired
    private AthleteRepository athleteRepository;

    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    public List<Athlete> searchAthletes(String query) {
        return athleteRepository.search(query);
    }

    public Athlete saveAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void deleteAthlete(Long id) {
        athleteRepository.deleteById(id);
    }

    public List<Athlete> getAllAthletes() {
        return findAll();
    }
}
