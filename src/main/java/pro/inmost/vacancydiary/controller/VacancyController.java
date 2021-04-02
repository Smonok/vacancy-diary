package pro.inmost.vacancydiary.controller;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
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
import pro.inmost.vacancydiary.mapper.VacancyMapper;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.model.dto.VacancyDto;
import pro.inmost.vacancydiary.service.VacancyService;
import pro.inmost.vacancydiary.util.DateUtil;

@Slf4j
@RestController
@RequestMapping("/diary")
public class VacancyController {
    private static final int PAGE_SIZE = 5;
    private final VacancyService vacancyService;
    private final VacancyMapper vacancyMapper;

    @Autowired
    public VacancyController(VacancyService vacancyService, VacancyMapper vacancyMapper) {
        this.vacancyService = vacancyService;
        this.vacancyMapper = vacancyMapper;
    }

    @GetMapping("vacancies")
    public List<VacancyDto> findAll() {
        List<Vacancy> vacancies = vacancyService.findAll();
        return vacancyMapper.map(vacancies);
    }

    @GetMapping("vacancies/{id}")
    public ResponseEntity<VacancyDto> findById(@PathVariable(value = "id") Long id) {
        Vacancy vacancyById = vacancyService.findById(id);
        VacancyDto response = vacancyMapper.map(vacancyById);
        log.info("Founded vacancy: {} by id = {}", vacancyById, id);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("vacancies")
    public ResponseEntity<VacancyDto> create(@RequestBody VacancyDto vacancyDto) {
        throwExceptionIfStatusNotExists(vacancyDto.getStatus());
        vacancyDto.setLastStatusChange(DateUtil.getCurrentDate());

        Vacancy vacancy = vacancyMapper.map(vacancyDto);
        Vacancy created = vacancyService.create(vacancy);

        VacancyDto createdDto = vacancyMapper.map(created);
        return ResponseEntity.ok().body(createdDto);
    }

    @GetMapping("vacancies/users/{userId}")
    public ResponseEntity<Page<VacancyDto>> findByUserId(@PathVariable(value = "userId") Long userId,
        @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, value = PAGE_SIZE) Pageable pageable) {
        if (userId < 0) {
            return ResponseEntity.notFound().build();
        }

        Page<Vacancy> page = vacancyService.findAllByUserId(userId, pageable);
        Page<VacancyDto> pageDto = page.map(vacancyMapper::map);

        return ResponseEntity.ok().body(pageDto);
    }

    @GetMapping("vacancies/statuses/{status}")
    public ResponseEntity<Set<VacancyDto>> findByStatus(@PathVariable(value = "status") String status) {
        if (!Status.collectValues().contains(status)) {
            return ResponseEntity.notFound().build();
        }

        Set<Vacancy> vacanciesByStatus = vacancyService.findAllByStatus(status);
        Set<VacancyDto> vacanciesByStatusDto = vacancyMapper.map(vacanciesByStatus);

        return ResponseEntity.ok().body(vacanciesByStatusDto);
    }

    @GetMapping("vacancies/companies/{company}")
    public ResponseEntity<Set<VacancyDto>> findByCompany(@PathVariable(value = "company") String company) {
        if (StringUtils.isEmpty(company)) {
            return ResponseEntity.notFound().build();
        }

        Set<Vacancy> vacanciesByCompany = vacancyService.findAllByCompanyName(company);
        Set<VacancyDto> vacanciesByCompanyDto = vacancyMapper.map(vacanciesByCompany);

        return ResponseEntity.ok().body(vacanciesByCompanyDto);
    }

    @PutMapping("vacancies/{id}")
    public ResponseEntity<VacancyDto> update(@PathVariable(value = "id") Long id, @RequestBody VacancyDto vacancyDto) {
        Vacancy vacancyById = vacancyService.findById(id);
        String providedStatus = vacancyDto.getStatus();

        throwExceptionIfStatusNotExists(providedStatus);

        log.info("Vacancy to update: {}", vacancyById);
        vacancyMapper.update(vacancyDto, vacancyById);

        if (providedStatus != null) {
            vacancyById.setLastStatusChange(DateUtil.getCurrentDate());
        }

        log.info("Updated vacancy: {}", vacancyById);
        vacancyService.create(vacancyById);

        VacancyDto vacancyByIdDto = vacancyMapper.map(vacancyById);
        return ResponseEntity.ok().body(vacancyByIdDto);
    }

    private void throwExceptionIfStatusNotExists(String providedStatus) {
        if (providedStatus != null && !Status.isValueExists(providedStatus)) {
            throw new IllegalArgumentException("Status does not exist");
        }
    }
}
