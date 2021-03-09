package pro.inmost.vacancydiary.service.impl;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.repository.UserRepository;
import pro.inmost.vacancydiary.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "Cannot find user with id = ";
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public User create(User user) {
        if (user == null) {
            return new User();
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User userById = findById(id);

        userRepository.delete(userById);
    }

    @Override
    public User findByName(String name) {
        if (StringUtils.isBlank(name)) {
            return new User();
        }

        return userRepository.findByName(name);
    }

    @Override
    public User register(User user) {
        if (isEmptyUser(user)) {
            return new User();
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User toSave = new User(user.getName(), user.getEmail(), encodedPassword);
        return userRepository.save(toSave);
    }

    private boolean isEmptyUser(User user) {
        if (user == null) {
            return true;
        }

        String userName = user.getName();
        String userPassword = user.getPassword();
        String userEmail = user.getEmail();

        return StringUtils.isBlank(userName) && StringUtils.isBlank(userPassword) && StringUtils.isBlank(userEmail);
    }
}
