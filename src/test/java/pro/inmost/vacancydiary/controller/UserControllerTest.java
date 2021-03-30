package pro.inmost.vacancydiary.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import pro.inmost.vacancydiary.model.User;
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
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void findAllShouldReturnAllUsers() throws Exception {
        List<User> anyUsers = Arrays.asList(new User(), new User());
        when(userService.findAll()).thenReturn(anyUsers);

        RequestBuilder request = get("/diary/users").contentType(MediaType.APPLICATION_JSON);

        String response = getResponseContent(request, status().isOk());
        List<User> users = objectMapper.readValue(response, usersList);

        int expectedUsersNumber = 2;
        int actualUsersNumber = users.size();

        assertEquals(expectedUsersNumber, actualUsersNumber);
    }

    @Test
    void findByIdShouldReturnUserWithProvidedIdWhenUserExists() throws JsonProcessingException {
        long id = 1;
        User user = new User();
        user.setId(id);
        when(userService.findById(ArgumentMatchers.anyLong())).thenReturn(user);

        RequestBuilder request = get("/diary/users/{id}", id).contentType(MediaType.APPLICATION_JSON);

        String response = getResponseContent(request, status().isOk());
        User userById = objectMapper.readValue(response, User.class);

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
        when(userService.create(ArgumentMatchers.any(User.class))).thenReturn(user);

        String requestJson = convertToJson(user);

        RequestBuilder request = post("/diary/users")
            .contentType(MediaType.APPLICATION_JSON).content(requestJson);

        String response = getResponseContent(request, status().isOk());
        User created = objectMapper.readValue(response, User.class);
        String actualEmail = created.getEmail();

        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void updateShouldChangeOnlyProvidedFieldsWhenUserWithProvidedIdExists() throws JsonProcessingException {
        String initialEmail = "mail@gmail.com";
        String expectedEmail = "mail22@ukr.net";
        String password = "password";
        User userBeforeUpdate = new User("Name", initialEmail, password);
        User userAfterUpdate = new User(null, expectedEmail, null);

        when(userService.findById(ArgumentMatchers.anyLong())).thenReturn(userBeforeUpdate);
        when(userService.create(ArgumentMatchers.any(User.class))).thenReturn(userAfterUpdate);

        String requestJson = convertToJson(userAfterUpdate);

        RequestBuilder request = put("/diary/users/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON).content(requestJson);

        String response = getResponseContent(request, status().isOk());
        User updated = objectMapper.readValue(response, User.class);

        assertEquals(expectedEmail, updated.getEmail());
        assertEquals(password, updated.getPassword());
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
