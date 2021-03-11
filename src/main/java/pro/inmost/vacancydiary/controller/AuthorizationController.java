package pro.inmost.vacancydiary.controller;

import static pro.inmost.vacancydiary.util.ResponseUtil.createResponse;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
public class AuthorizationController {
    private final UserRepository userRepository;

    @Autowired
    public AuthorizationController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        String currentUserEmail = user.getEmail();
        String currentUserPassword = user.getPassword();

        for (User other : users) {
            String email = other.getEmail();
            String password = other.getPassword();

            if (email.equals(currentUserEmail) && password.equals(currentUserPassword)) {
                return createResponse(LoginStatus.SUCCESS);
            }
        }

        return createResponse(LoginStatus.NO_SUCH_EMAIL);
    }
}
