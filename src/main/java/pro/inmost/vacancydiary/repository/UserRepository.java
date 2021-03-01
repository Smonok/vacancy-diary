package pro.inmost.vacancydiary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.inmost.vacancydiary.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
