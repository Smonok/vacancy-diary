package pro.inmost.vacancydiary.controller;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.repository.VacancyRepository;

@RestController
@RequestMapping("/diary")
public class VacancyController {
    private static final int PAGE_SIZE = 5;
    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @GetMapping("vacancies")
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @GetMapping("vacancies/{id}")
    public ResponseEntity<Vacancy> findById(@PathVariable(value = "id") Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find vacancy with id = " + id));

        return ResponseEntity.ok().body(vacancy);
    }

    @PostMapping("vacancies")
    public ResponseEntity<Vacancy> create(@RequestBody Vacancy vacancy) {
        if (vacancy == null || !Status.collectValues().contains(vacancy.getStatus())) {
            return ResponseEntity.badRequest().body(null);
        }
        Timestamp current = new Timestamp(new Date().getTime());
        vacancy.setLastStatusChange(current);

        vacancyRepository.save(vacancy);

        return ResponseEntity.ok().body(vacancy);
    }

    @GetMapping("vacancies/users/{userId}")
    public ResponseEntity<Page<Vacancy>> findByUserId(@PathVariable(value = "userId") Long userId,
        @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, value = PAGE_SIZE) Pageable pageable) {
        if (userId < 1) {
            return ResponseEntity.badRequest().body(Page.empty());
        }

        Page<Vacancy> page = vacancyRepository.findAllByUserId(userId, pageable);

        return ResponseEntity.ok().body(page);
    }

    @GetMapping("vacancies/statuses/{status}")
    public ResponseEntity<Set<Vacancy>> findByStatus(@PathVariable(value = "status") String status) {
        if (!Status.collectValues().contains(status)) {
            return ResponseEntity.ok().body(Collections.emptySet());
        }

        Set<Vacancy> vacanciesByStatus = vacancyRepository.findAllByStatus(status);

        return ResponseEntity.ok().body(vacanciesByStatus);
    }

    @GetMapping("vacancies/companies/{company}")
    public ResponseEntity<Set<Vacancy>> findByCompany(@PathVariable(value = "company") String company) {
        if (StringUtils.isEmpty(company)) {
            return ResponseEntity.ok().body(Collections.emptySet());
        }

        Set<Vacancy> vacanciesByCompany = vacancyRepository.findAllByCompanyName(company);

        return ResponseEntity.ok().body(vacanciesByCompany);
    }

    @PutMapping("vacancies/{id}")
    public ResponseEntity<Vacancy> update(@PathVariable(value = "id") Long id, @RequestBody Vacancy vacancy) {
        Vacancy vacancyById = vacancyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("USER_NOT_FOUND_MESSAGE" + id));

        partialUpdate(vacancyById, vacancy);
        vacancyRepository.save(vacancyById);

        return ResponseEntity.ok().body(vacancyById);
    }

    private void partialUpdate(Vacancy destination, Vacancy source) {
        if (destination != null && source != null) {
            if (!source.getCompanyName().isEmpty()) {
                destination.setCompanyName(source.getCompanyName());
            }
            if (!StringUtils.isEmpty(source.getStatus()) && Status.collectValues().contains(source.getStatus())) {
                Date date = new Date();
                Timestamp current = new Timestamp(date.getTime());

                destination.setStatus(source.getStatus());
                destination.setLastStatusChange(current);
            }
            if (source.getExpectedSalary() != 0) {
                destination.setExpectedSalary(source.getExpectedSalary());
            }
            if (!StringUtils.isEmpty(source.getLink())) {
                destination.setLink(source.getLink());
            }
            if (!StringUtils.isEmpty(source.getPosition())) {
                destination.setPosition(source.getPosition());
            }
            if (!StringUtils.isEmpty(source.getRecruiterContacts())) {
                destination.setRecruiterContacts(source.getRecruiterContacts());
            }
            if (source.getUsers() != null && !source.getUsers().isEmpty()) {
                destination.setUsers(source.getUsers());
            }
        }
    }


}
