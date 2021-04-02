package pro.inmost.vacancydiary.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pro.inmost.vacancydiary.mapper.UserMapper;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.dto.UserDto;
import pro.inmost.vacancydiary.service.UserService;

@Slf4j
@RestController
@RequestMapping("/diary")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("users")
    public List<UserDto> findAll() {
        List<User> users = userService.findAll();
        return userMapper.map(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
        User userById = userService.findById(id);
        UserDto response = userMapper.map(userById);
        log.info("Founded user: {} by id = {}", userById, id);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("users")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        if (userDto == null) {
            return new ResponseEntity<>(new UserDto(), HttpStatus.BAD_REQUEST);
        }

        String password = userDto.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        userDto.setPassword(encodedPassword);

        User user = userMapper.map(userDto);
        User created = userService.create(user);
        UserDto createdDto = userMapper.map(created);

        log.info("Created user: {}", created);
        return ResponseEntity.ok().body(createdDto);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<UserDto> update(@PathVariable(value = "id") Long id, @RequestBody UserDto userDto) {
        User userById = userService.findById(id);

        log.info("User to update: {}", userById);
        userMapper.update(userDto, userById);
        log.info("Updated user: {}", userById);
        userService.create(userById);

        UserDto userByIdDto = userMapper.map(userById);
        return ResponseEntity.ok().body(userByIdDto);
    }

    @DeleteMapping("users/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
