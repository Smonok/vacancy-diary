package pro.inmost.vacancydiary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;

@Repository
public interface VacancyRepository  extends JpaRepository<Vacancy, Long>  {
}
