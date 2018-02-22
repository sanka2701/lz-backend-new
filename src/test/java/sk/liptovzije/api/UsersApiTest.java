package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sk.liptovzije.api.security.WebSecurityConfig;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.encrypt.NaiveEncryptService;
import sk.liptovzije.core.service.jwt.IJwtService;
import sk.liptovzije.core.service.user.IUserService;
import sk.liptovzije.core.service.user.UserService;

import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersApi.class)
@Import({WebSecurityConfig.class, UserService.class, NaiveEncryptService.class})
public class UsersApiTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private IJwtService jwtService;

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void should_create_user_success() throws Exception {
        Long id = 0L;
        String email = "john@jacob.com";
        String username = "johnjacob";
        String password = "123";

        User user = new User(email, username, password);
        user.setId(id);

        RegisterParam registration = new RegisterParam(email, username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        when(jwtService.toToken(any())).thenReturn(password);
        when(userService.findByUsername(eq(username))).thenReturn(Optional.empty());
        when(userService.findByEmail(eq(email))).thenReturn(Optional.empty());
        when(userService.findById(eq(id))).thenReturn(Optional.of(user));
        when(userService.save(any())).thenReturn(Optional.of(user));

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users")
            .then()
            .statusCode(201)
            .body("user.email", equalTo(email))
            .body("user.username", equalTo(username))
            .body("user.token", equalTo("123"));

        verify(userService).save(any());
    }

    @Test
    public void should_create_user_without_email_success() throws Exception {
        Long id = 0L;
        String username = "johnjacob";
        String password = "123";

        User user = new User("", username, password);
        user.setId(id);

        RegisterParam registration = new RegisterParam("", username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        when(jwtService.toToken(any())).thenReturn(password);
        when(userService.findByUsername(eq(username))).thenReturn(Optional.empty());
        when(userService.findByEmail(any())).thenReturn(Optional.empty());
        when(userService.findById(eq(id))).thenReturn(Optional.of(user));
        when(userService.save(any())).thenReturn(Optional.of(user));

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("user.email", equalTo(""))
                .body("user.username", equalTo(username))
                .body("user.token", equalTo("123"));

        verify(userService).save(any());
    }

    @Test
    public void should_show_error_message_for_blank_username() throws Exception {
        String email = "john@jacob.com";
        String username = "";
        String password = "123";

        RegisterParam registration = new RegisterParam(email, username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users")
            .then()
            .statusCode(422)
            .body("errors.username[0]", equalTo("can't be empty"));
    }

    @Test
    public void should_show_error_message_for_blank_password() throws Exception {
        String email = "john@jacob.com";
        String username = "johnjacob";
        String password = "";

        RegisterParam registration = new RegisterParam(email, username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/users")
                .then()
                .statusCode(422)
                .body("errors.password[0]", equalTo("can't be empty"));
    }

    @Test
    public void should_show_error_message_for_invalid_email() throws Exception {
        String email = "johnxjacob.com";
        String username = "johnjacob";
        String password = "123";

        RegisterParam registration = new RegisterParam(email, username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users")
            .then()
            .statusCode(422)
            .body("errors.email[0]", equalTo("should be an email"));

    }

    @Test
    public void should_show_error_for_duplicated_username() throws Exception {
        Long id = 0L;
        String email = "john@jacob.com";
        String username = "johnjacob";
        String password = "123";

        User user = new User("otherjohn@jacob.com", username, "321");
        user.setId(id);

        RegisterParam registration = new RegisterParam(email, username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        when(userService.findByUsername(eq(username))).thenReturn(Optional.of(user));
        when(userService.findByEmail(any())).thenReturn(Optional.empty());

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users")
            .then()
            .statusCode(422)
            .body("errors.username[0]", equalTo("duplicated username"));
    }

    @Test
    public void should_show_error_for_duplicated_email() throws Exception {
        Long id = 0L;
        String email = "john@jacob.com";
        String username = "johnjacob";
        String password = "123";

        User user = new User(email, "otherjohnjacob", "321");
        user.setId(id);

        RegisterParam registration = new RegisterParam(email, username, password);
        String param = new ObjectMapper().writeValueAsString(registration);

        when(userService.findByEmail(eq(email))).thenReturn(Optional.of(user));
        when(userService.findByUsername(eq(username))).thenReturn(Optional.empty());

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users")
            .then()
            .statusCode(422)
            .body("errors.email[0]", equalTo("duplicated email"));
    }

    @Test
    public void should_login_success() throws Exception {
        Long id = 0L;
        String email = "john@jacob.com";
        String username = "johnjacob2";
        String password = "123";

        User user = new User(email, username, password);
        user.setId(id);

        LoginParam login = new LoginParam(username, password);
        String param = new ObjectMapper().writeValueAsString(login);

        when(userService.findByUsername(eq(username))).thenReturn(Optional.of(user));
        when(jwtService.toToken(any())).thenReturn("123");

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users/login")
            .then()
            .statusCode(200)
            .body("user.email", equalTo(email))
            .body("user.username", equalTo(username))
            .body("user.password", equalTo(null))
            .body("user.token", equalTo("123"));
    }

    @Test
    public void should_fail_login_with_wrong_password() throws Exception {
        Long id = 0L;
        String email = "john@jacob.com";
        String username = "johnjacob2";
        String password = "123";

        User user = new User(email, username, "otherPassword");
        user.setId(id);

        LoginParam login = new LoginParam(username, password);
        String param = new ObjectMapper().writeValueAsString(login);

        when(userService.findByUsername(eq(username))).thenReturn(Optional.of(user));

        given()
            .contentType("application/json")
            .body(param)
            .when()
            .post("/users/login")
            .then()
            .statusCode(422)
            .body("errors.password[0]", equalTo("invalid email or password"));
    }
}
