package pro.inmost.vacancydiary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.dto.UserDto;

@ExtendWith(SpringExtension.class)
public class UserMapperTest {
    private static final UserMapper userMapper = new UserMapperImpl();

    @Test
    void mapShouldReturnUserWithSameFieldsValuesWhenUserDtoParameter() {
        UserDto userDto = createTestUserDto();
        User expected = createTestUser();
        User actual = userMapper.map(userDto);

        assertEquals(expected, actual);
    }

    @Test
    void mapShouldReturnUserDtoWithSameFieldsValuesAsUserWhenUserParameter() {
        User user = createTestUser();
        UserDto expected = createTestUserDto();
        UserDto actual = userMapper.map(user);

        assertEquals(expected, actual);
    }

    @Test
    void mapShouldReturnListOfUserDtoThatEqualsToProvidedListWhenListOfUsersParameter() {
        List<User> users = Collections.singletonList(createTestUser());
        List<UserDto> expected = Collections.singletonList(createTestUserDto());
        List<UserDto> actual = userMapper.map(users);

        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateOnlyUserEmailWithIdIgnoredWhenOnlyUserDtoEmailNonNull() {
        User userToUpdate = createTestUser();
        UserDto userDto = new UserDto();
        String updatedEmail = "new_email@gmail.com";
        userDto.setEmail(updatedEmail);

        userMapper.update(userDto, userToUpdate);
        User expected = createTestUser();
        expected.setEmail(updatedEmail);

        assertEquals(expected, userToUpdate);
    }

    private User createTestUser() {
        return new User(1L, "Name", "mail@gmail.com", "password", Collections.emptySet());
    }

    private UserDto createTestUserDto() {
        return new UserDto(1L, "Name", "mail@gmail.com", "password", Collections.emptySet());
    }
}
