package pro.inmost.vacancydiary.controller;

import java.util.List;
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
        if(user == null) {
            return new User();
        }

        return userRepository.save(user);
    }
}
