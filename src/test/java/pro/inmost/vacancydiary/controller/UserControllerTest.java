package pro.inmost.vacancydiary.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.inmost.vacancydiary.mapper.UserMapper;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.dto.UserDto;
import pro.inmost.vacancydiary.service.UserService;

@Slf4j
@WebMvcTest(UserController.class)
@WebAppConfiguration
@ContextConfiguration
public class UserControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CollectionType usersList = objectMapper.getTypeFactory()
        .constructCollectionType(List.class, User.class);
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, userMapper)).build();
    }

    @Test
    void findAllShouldReturnAllUsers() throws Exception {
        List<User> anyUsers = Arrays.asList(new User(), new User());
        List<UserDto> anyUsersDto = Arrays.asList(new UserDto(), new UserDto());
        when(userService.findAll()).thenReturn(anyUsers);
        when(userMapper.map(anyUsers)).thenReturn(anyUsersDto);

        RequestBuilder request = get("/diary/users").contentType(MediaType.APPLICATION_JSON);

        String response = getResponseContent(request, status().isOk());
        List<UserDto> users = objectMapper.readValue(response, usersList);

        int expectedUsersNumber = 2;
        int actualUsersNumber = users.size();

        assertEquals(expectedUsersNumber, actualUsersNumber);
    }

    @Test
    void findByIdShouldReturnUserWithProvidedIdWhenUserExists() throws JsonProcessingException {
        long id = 1;
        User user = new User(id, null, null, null, null);
        UserDto userDto = new UserDto(id, null, null, null, null);
        when(userService.findById(anyLong())).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDto);

        RequestBuilder request = get("/diary/users/{id}", id).contentType(MediaType.APPLICATION_JSON);

        String response = getResponseContent(request, status().isOk());
        UserDto userById = objectMapper.readValue(response, UserDto.class);

        assertEquals(id, userById.getId());
    }

    @Test
    void createShouldReturnBadRequestWithEmptyUserWhenUserEqualsNull() {
        String requestJson = convertToJson(null);

        RequestBuilder request = post("/diary/users")
            .contentType(MediaType.APPLICATION_JSON).content(requestJson);

        String response = getResponseContent(request, status().isBadRequest());
        String expected = "";

        assertEquals(expected, response);
    }

    @Test
    void createShouldReturnCreatedUserWhenUserNotNull() throws JsonProcessingException {
        String expectedEmail = "mail1@ukr.net";
        User user = new User("Name", expectedEmail, "password");
        UserDto userDto = new UserDto(1L, "Name", expectedEmail, "password", Collections.emptySet());
        when(userService.create(ArgumentMatchers.any(User.class))).thenReturn(user);
        when(userMapper.map(any(User.class))).thenReturn(userDto);
        when(userMapper.map(any(UserDto.class))).thenReturn(user);

        String requestJson = convertToJson(user);

        RequestBuilder request = post("/diary/users")
            .contentType(MediaType.APPLICATION_JSON).content(requestJson);

        String response = getResponseContent(request, status().isOk());
        UserDto created = objectMapper.readValue(response, UserDto.class);
        String actualEmail = created.getEmail();

        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void updateShouldChangeOnlyProvidedFieldsWhenUserWithProvidedIdExists() throws JsonProcessingException {
        String initialEmail = "mail@gmail.com";
        String expectedEmail = "mail22@ukr.net";
        String password = "password";
        String name = "Name";
        User userBeforeUpdate = new User(name, initialEmail, password);
        User userAfterUpdate = new User(name, expectedEmail, password);
        UserDto userAfterUpdateDto = new UserDto(1L, name, expectedEmail, password, Collections.emptySet());

        when(userService.findById(anyLong())).thenReturn(userBeforeUpdate);
        when(userService.create(any(User.class))).thenReturn(userAfterUpdate);
        when(userMapper.map(any(User.class))).thenReturn(userAfterUpdateDto);

        String requestJson = convertToJson(userAfterUpdate);

        RequestBuilder request = put("/diary/users/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON).content(requestJson);

        String response = getResponseContent(request, status().isOk());
        UserDto updated = objectMapper.readValue(response, UserDto.class);

        assertEquals(expectedEmail, updated.getEmail());
        assertEquals(password, updated.getPassword());
        assertNotNull(updated.getName());
    }

    @Test
    void deleteShouldReturnTrueResponseWhenUserDeletedById() {
        RequestBuilder request = delete("/diary/users/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON);

        String response = getResponseContent(request, status().isOk());
        String expected = "{\"deleted\":true}";

        assertEquals(expected, response);
    }

    private String getResponseContent(RequestBuilder request, ResultMatcher expectedStatus) {
        try {
            MvcResult result = mockMvc.perform(request).andExpect(expectedStatus).andReturn();

            return result.getResponse().getContentAsString();
        } catch (Exception e) {
            log.error("Cannot perform request", e);
        }

        return "";
    }

    private String convertToJson(User user) {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert {} to string", user, e);
        }

        return "{ }";
    }
}
