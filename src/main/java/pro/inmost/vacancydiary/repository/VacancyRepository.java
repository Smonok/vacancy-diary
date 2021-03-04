package pro.inmost.vacancydiary.repository;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Query("select v from Vacancy v where (select u from User u where u.id = ?1) member of v.users")
    Page<Vacancy> findAllByUserId(Long userId, Pageable pageable);

    @Query("select v from Vacancy v where ?1 member of v.users and v.status = ?2")
    Set<Vacancy> findAllByUserAndStatus(User user, String status);

    Set<Vacancy> findAllByStatus(String status);

    Set<Vacancy> findAllByCompanyName(String companyName);
}
