package pro.inmost.vacancydiary.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.inmost.vacancydiary.model.Vacancy;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Query("select v from Vacancy v where (select u from User u where u.id = ?1) member of v.users")
    Page<Vacancy> findByUser(Long userId, Pageable pageable);

    List<Vacancy> findAllByStatus(String status);

    List<Vacancy> findAllByCompanyName(String companyName);
}
