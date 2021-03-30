package pro.inmost.vacancydiary.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.service.UserService;
import pro.inmost.vacancydiary.util.UserUtil;

@RestController
@RequestMapping("/diary")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findById(@PathVariable(value = "id") Long id) {
        User response = userService.findById(id);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("users")
    public ResponseEntity<User> create(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        }

        String password = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);

        User created = userService.create(user);
        return ResponseEntity.ok().body(created);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<User> update(@PathVariable(value = "id") Long id, @RequestBody User user) {
        User userById = userService.findById(id);

        UserUtil.partialUpdate(userById, user);
        userService.create(userById);

        return ResponseEntity.ok().body(userById);
    }

    @DeleteMapping("users/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
