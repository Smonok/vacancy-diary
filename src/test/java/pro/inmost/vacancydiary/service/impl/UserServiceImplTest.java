package pro.inmost.vacancydiary.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createShouldReturnCreatedUserWhenUserNonNull() {
        User user = new User("Name", "mail1@ukr.net", "password", Collections.emptySet());
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        User returnedUser = userService.create(user);

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(user.getEmail(), returnedUser.getEmail());
    }

    @Test
    void createShouldReturnEmptyUserWhenUserIsNull() {
        User expected = new User();
        User actual = userService.create(null);

        assertEquals(expected, actual);
    }

    @Test
    void findAllShouldReturnAllUsers() {
        List<User> anyUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(anyUsers);

        List<User> users = userService.findAll();

        assertEquals(anyUsers.size(), users.size());
    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionWhenNoUserFound() {
        long id = 0;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void findByIdShouldReturnUserWithProvidedIdWhenSuchUserExists() {
        long id = 0;
        User expected = new User();
        expected.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(expected));

        User actual = userService.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void deleteShouldThrowEntityNotFoundExceptionWhenNoUserFound() {
        long id = 0;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.delete(id));
    }

    @Test
    void findByNameShouldReturnEmptyUserWhenNameIsBlank() {
        User expected = new User();
        User actual = userService.findByName(null);
        assertEquals(expected, actual);

        actual = userService.findByName("");
        assertEquals(expected, actual);

        actual = userService.findByName("      ");
        assertEquals(expected, actual);
    }

    @Test
    void findByNameShouldReturnUserWhenNameNotBlank() {
        String name = "name";
        User expected = new User(name, "mail1@ukr.net", "password");
        when(userRepository.findByName(ArgumentMatchers.anyString())).thenReturn(expected);

        User actual = userService.findByName(name);

        assertEquals(expected, actual);
    }

    @Test
    void registerShouldReturnEmptyUserWhenUserNull() {
        assertEquals(new User(), userService.register(null));
    }

    @Test
    void registerShouldReturnEmptyUserWhenBlankNameAndEmailAndPassword() {
        User emptyUser = new User("", "", "");
        emptyUser.setId(1);
        emptyUser.setVacancies(Sets.newHashSet(new Vacancy(), new Vacancy()));

        User expected = new User();
        User actual = userService.register(emptyUser);

        assertEquals(expected, actual);
    }
}
