package pro.inmost.vacancydiary.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.repository.VacancyRepository;
import pro.inmost.vacancydiary.service.VacancyService;

@Service
public class VacancyServiceImpl implements VacancyService {
    private static final String VACANCY_NOT_FOUND_MESSAGE = "Cannot find vacancy with id = ";
    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public Vacancy findById(Long id) {
        return vacancyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(VACANCY_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public Vacancy create(Vacancy vacancy) {
        if (vacancy == null) {
            return new Vacancy();
        }

        return vacancyRepository.save(vacancy);
    }

    @Override
    public Page<Vacancy> findAllByUserId(Long userId, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }

        return vacancyRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Set<Vacancy> findAllByStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            return Collections.emptySet();
        }

        return vacancyRepository.findAllByStatus(status);
    }

    @Override
    public Set<Vacancy> findAllByCompanyName(String company) {
        if (StringUtils.isEmpty(company)) {
            return Collections.emptySet();
        }

        return vacancyRepository.findAllByCompanyName(company);
    }

    @Override
    public Set<Vacancy> findAllByUserAndStatus(User user, String status) {
        if (user == null || StringUtils.isEmpty(status)) {
            return Collections.emptySet();
        }

        return vacancyRepository.findAllByUserAndStatus(user, status);
    }
}
