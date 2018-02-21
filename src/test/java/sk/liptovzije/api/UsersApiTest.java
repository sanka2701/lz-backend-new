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
import sk.liptovzije.application.user.UserData;
import sk.liptovzije.core.service.encrypt.NaiveEncryptService;
import sk.liptovzije.core.service.jwt.IJwtService;
import sk.liptovzije.core.service.user.IUserService;
import sk.liptovzije.core.service.user.UserService;

import java.util.HashMap;
import java.util.Map;
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

        when(jwtService.toToken(any())).thenReturn(password);
        User user = new User(email, username, password);
        user.setId(id);

        when(userService.findByUsername(eq(username))).thenReturn(Optional.empty());
        when(userService.findByEmail(eq(email))).thenReturn(Optional.empty());
        when(userService.findById(eq(id))).thenReturn(Optional.of(user));
        when(userService.save(any())).thenReturn(Optional.of(user));

        ObjectMapper mapper = new ObjectMapper();
        String param = mapper.writeValueAsString(user);

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

//    @Test
//    public void should_show_error_message_for_blank_username() throws Exception {
//
//        String email = "john@jacob.com";
//        String username = "";
//
//        Map<String, Object> param = prepareRegisterParameter(email, username);
//
//        given()
//            .contentType("application/json")
//            .body(param)
//            .when()
//            .post("/users")
//            .then()
//            .statusCode(422)
//            .body("errors.username[0]", equalTo("can't be empty"));
//    }
//
//    @Test
//    public void should_show_error_message_for_invalid_email() throws Exception {
//        String email = "johnxjacob.com";
//        String username = "johnjacob";
//
//        Map<String, Object> param = prepareRegisterParameter(email, username);
//
//        given()
//            .contentType("application/json")
//            .body(param)
//            .when()
//            .post("/users")
//            .then()
//            .statusCode(422)
//            .body("errors.email[0]", equalTo("should be an email"));
//
//    }
//
//    @Test
//    public void should_show_error_for_duplicated_username() throws Exception {
//        String email = "john@jacob.com";
//        String username = "johnjacob";
//
//        when(userService.findByUsername(eq(username))).thenReturn(Optional.of(new User(
//            email, username, "123", "bio", ""
//        )));
//        when(userService.findByEmail(any())).thenReturn(Optional.empty());
//
//        Map<String, Object> param = prepareRegisterParameter(email, username);
//
//        given()
//            .contentType("application/json")
//            .body(param)
//            .when()
//            .post("/users")
//            .then()
//            .statusCode(422)
//            .body("errors.username[0]", equalTo("duplicated username"));
//    }
//
//    @Test
//    public void should_show_error_for_duplicated_email() throws Exception {
//        String email = "john@jacob.com";
//        String username = "johnjacob2";
//
//        when(userService.findByEmail(eq(email))).thenReturn(Optional.of(new User(
//            email, username, "123", "bio", ""
//        )));
//
//        when(userService.findByUsername(eq(username))).thenReturn(Optional.empty());
//
//        Map<String, Object> param = prepareRegisterParameter(email, username);
//
//        given()
//            .contentType("application/json")
//            .body(param)
//            .when()
//            .post("/users")
//            .then()
//            .statusCode(422)
//            .body("errors.email[0]", equalTo("duplicated email"));
//    }
//
//    private HashMap<String, Object> prepareRegisterParameter(final String email, final String username) {
//        return new HashMap<String, Object>() {{
//            put("user", new HashMap<String, Object>() {{
//                put("email", email);
//                put("password", "johnnyjacob");
//                put("username", username);
//            }});
//        }};
//    }
//
//    @Test
//    public void should_login_success() throws Exception {
//        String email = "john@jacob.com";
//        String username = "johnjacob2";
//        String password = "123";
//
//        User user = new User(email, username, password, "", defaultAvatar);
//        UserData userData = new UserData("123", email, username, "", defaultAvatar);
//
//        when(userService.findByEmail(eq(email))).thenReturn(Optional.of(user));
//        when(userReadService.findByUsername(eq(username))).thenReturn(userData);
//        when(userReadService.findById(eq(user.getId()))).thenReturn(userData);
//        when(jwtService.toToken(any())).thenReturn("123");
//
//        Map<String, Object> param = new HashMap<String, Object>() {{
//            put("user", new HashMap<String, Object>() {{
//                put("email", email);
//                put("password", password);
//            }});
//        }};
//
//        given()
//            .contentType("application/json")
//            .body(param)
//            .when()
//            .post("/users/login")
//            .then()
//            .statusCode(200)
//            .body("user.email", equalTo(email))
//            .body("user.username", equalTo(username))
//            .body("user.bio", equalTo(""))
//            .body("user.image", equalTo(defaultAvatar))
//            .body("user.token", equalTo("123"));;
//    }
//
//    @Test
//    public void should_fail_login_with_wrong_password() throws Exception {
//        String email = "john@jacob.com";
//        String username = "johnjacob2";
//        String password = "123";
//
//        User user = new User(email, username, password, "", defaultAvatar);
//        UserData userData = new UserData(user.getId(), email, username, "", defaultAvatar);
//
//        when(userService.findByEmail(eq(email))).thenReturn(Optional.of(user));
//        when(userReadService.findByUsername(eq(username))).thenReturn(userData);
//
//        Map<String, Object> param = new HashMap<String, Object>() {{
//            put("user", new HashMap<String, Object>() {{
//                put("email", email);
//                put("password", "123123");
//            }});
//        }};
//
//        given()
//            .contentType("application/json")
//            .body(param)
//            .when()
//            .post("/users/login")
//            .then()
//            .statusCode(422)
//            .body("errors.password[0]", equalTo("invalid email or password"));
//    }
}
