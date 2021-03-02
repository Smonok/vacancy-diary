package pro.inmost.vacancydiary.controller;

import static pro.inmost.vacancydiary.util.ResponseUtil.*;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.repository.UserRepository;
import pro.inmost.vacancydiary.status.LoginStatus;
import pro.inmost.vacancydiary.status.RegisterStatus;
import pro.inmost.vacancydiary.status.ResponseStatus;

@RestController
@RequestMapping("/diary")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("users")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findById(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + id));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("users")
    public User create(@RequestBody User user) {
        if (user == null) {
            return new User();
        }

        return userRepository.save(user);
    }

    @PostMapping("users/register")
    public Map<String, ResponseStatus> register(@RequestBody User user) {
        if (user == null) {
            return createResponse(RegisterStatus.FAILURE);
        }

        List<User> users = userRepository.findAll();
        String currentUserEmail = user.getEmail();

        for (User other : users) {
            String otherEmail = other.getEmail();

            if (otherEmail.equals(currentUserEmail)) {
                return createResponse(RegisterStatus.USER_ALREADY_EXISTS);
            }
        }

        userRepository.save(user);
        return createResponse(RegisterStatus.SUCCESS);
    }

    @PostMapping("users/login")
    public Map<String, ResponseStatus> loginUser(@RequestBody User user) {
        List<User> users = userRepository.findAll();

        for (User other : users) {
            if (other.equals(user)) {
                return createResponse(LoginStatus.SUCCESS);
            }
        }

        return createResponse(LoginStatus.NO_SUCH_EMAIL);
    }
}
