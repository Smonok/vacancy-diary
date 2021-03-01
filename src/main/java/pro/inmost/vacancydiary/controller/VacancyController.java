package pro.inmost.vacancydiary.controller;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.repository.VacancyRepository;

@RestController
@RequestMapping("/diary")
public class VacancyController {
    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @GetMapping("vacancies")
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @GetMapping("/vacancies/{id}")
    public ResponseEntity<Vacancy> findById(@PathVariable(value = "id") Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find vacancy with id = " + id));

        return ResponseEntity.ok().body(vacancy);
    }

    @PostMapping("vacancies")
    public Vacancy create(@RequestBody Vacancy vacancy) {
        if (vacancy == null) {
            return new Vacancy();
        }

        return vacancyRepository.save(vacancy);
    }
}
