package pro.inmost.vacancydiary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.inmost.vacancydiary.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name = ?1")
    User findByName(String name);
}
