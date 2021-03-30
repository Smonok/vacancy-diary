package pro.inmost.vacancydiary.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import pro.inmost.vacancydiary.service.VacancyService;
import pro.inmost.vacancydiary.util.VacancyUtil;

@RestController
@RequestMapping("/diary")
public class VacancyController {
    private static final int PAGE_SIZE = 5;
    private final VacancyService vacancyService;

    @Autowired
    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("vacancies")
    public List<Vacancy> findAll() {
        return vacancyService.findAll();
    }

    @GetMapping("vacancies/{id}")
    public ResponseEntity<Vacancy> findById(@PathVariable(value = "id") Long id) {
        Vacancy vacancy = vacancyService.findById(id);

        return ResponseEntity.ok().body(vacancy);
    }

    @PostMapping("vacancies")
    public ResponseEntity<Vacancy> create(@RequestBody Vacancy vacancy) {
        if (vacancy == null || !Status.collectValues().contains(vacancy.getStatus())) {
            return ResponseEntity.notFound().build();
        }
        Timestamp current = new Timestamp(new Date().getTime());
        vacancy.setLastStatusChange(current);

        vacancyService.create(vacancy);

        return ResponseEntity.ok().body(vacancy);
    }

    @GetMapping("vacancies/users/{userId}")
    public ResponseEntity<Page<Vacancy>> findByUserId(@PathVariable(value = "userId") Long userId,
        @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, value = PAGE_SIZE) Pageable pageable) {
        if (userId < 0) {
            return ResponseEntity.notFound().build();
        }

        Page<Vacancy> page = vacancyService.findAllByUserId(userId, pageable);

        return ResponseEntity.ok().body(page);
    }

    @GetMapping("vacancies/statuses/{status}")
    public ResponseEntity<Set<Vacancy>> findByStatus(@PathVariable(value = "status") String status) {
        if (!Status.collectValues().contains(status)) {
            return ResponseEntity.notFound().build();
        }

        Set<Vacancy> vacanciesByStatus = vacancyService.findAllByStatus(status);

        return ResponseEntity.ok().body(vacanciesByStatus);
    }

    @GetMapping("vacancies/companies/{company}")
    public ResponseEntity<Set<Vacancy>> findByCompany(@PathVariable(value = "company") String company) {
        if (StringUtils.isEmpty(company)) {
            return ResponseEntity.notFound().build();
        }

        Set<Vacancy> vacanciesByCompany = vacancyService.findAllByCompanyName(company);

        return ResponseEntity.ok().body(vacanciesByCompany);
    }

    @PutMapping("vacancies/{id}")
    public ResponseEntity<Vacancy> update(@PathVariable(value = "id") Long id, @RequestBody Vacancy vacancy) {
        Vacancy vacancyById = vacancyService.findById(id);

        VacancyUtil.partialUpdate(vacancyById, vacancy);
        vacancyService.create(vacancyById);

        return ResponseEntity.ok().body(vacancyById);
    }
}
