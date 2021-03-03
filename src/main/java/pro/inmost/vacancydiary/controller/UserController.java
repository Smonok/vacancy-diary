package pro.inmost.vacancydiary.controller;

import static pro.inmost.vacancydiary.util.ResponseUtil.createResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.repository.UserRepository;
import pro.inmost.vacancydiary.response.status.LoginStatus;
import pro.inmost.vacancydiary.response.status.RegisterStatus;
import pro.inmost.vacancydiary.response.status.ResponseStatus;

@RestController
@RequestMapping("/diary")
public class UserController {
    private static final String USER_NOT_FOUND_MESSAGE = "Cannot find user with id = ";
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
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + id));

        User response = new User(user.getEmail(), user.getPassword());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("users")
    public ResponseEntity<User> create(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        }

        User created = userRepository.save(user);
        return ResponseEntity.ok().body(created);
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
    public Map<String, ResponseStatus> login(@RequestBody User user) {
        List<User> users = userRepository.findAll();

        for (User other : users) {
            if (other.equals(user)) {
                return createResponse(LoginStatus.SUCCESS);
            }
        }

        return createResponse(LoginStatus.NO_SUCH_EMAIL);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable(value = "id") Long id, @RequestBody User user) {
        User userById = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + id));

        String updatedPassword = user.getPassword();

        userById.setPassword(updatedPassword);
        userRepository.save(userById);

        return ResponseEntity.ok().body(userById);
    }

    @DeleteMapping("users/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        User userById = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + id));

        userRepository.delete(userById);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
