package pro.inmost.vacancydiary.service;

import java.util.List;
import pro.inmost.vacancydiary.model.User;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User create(User user);

    void delete(Long id);

    User findByName(String name);

    User register(User user);
}
