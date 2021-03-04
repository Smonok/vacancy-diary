package pro.inmost.vacancydiary.service;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;

public interface VacancyService {

    List<Vacancy> findAll();

    Vacancy findById(Long id);

    Vacancy create(Vacancy vacancy);

    Page<Vacancy> findAllByUserId(Long userId, Pageable pageable);

    Set<Vacancy> findAllByStatus(String status);

    Set<Vacancy> findAllByCompanyName(String company);

    Set<Vacancy> findAllByUserAndStatus(User user, String status);
}
