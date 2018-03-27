package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import sk.liptovzije.api.security.WebSecurityConfig;
import sk.liptovzije.application.user.User;

import java.util.HashMap;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(CurrentUserApi.class)
@Import({WebSecurityConfig.class})
public class CurrentUserApiTest extends TestWithCurrentUser {

    @Autowired
    private MockMvc mvc;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void should_get_current_user_with_token() throws Exception {
        when(userService.findById(any())).thenReturn(Optional.of(user));

        given()
            .header("Authorization", "Token " + token)
            .contentType("application/json")
        .when()
            .get("/user")
        .then()
            .statusCode(200)
            .body("user.email", equalTo(email))
            .body("user.username", equalTo(username))
            .body("user.token", equalTo(token));
    }

    @Test
    public void should_get_401_without_token() throws Exception {
        given()
            .contentType("application/json")
        .when()
            .get("/user")
        .then()
            .statusCode(401);
    }

    @Test
    public void should_get_401_with_invalid_token() throws Exception {
        String invalidToken = "invalidtoken";
        when(jwtService.getSubFromToken(eq(invalidToken))).thenReturn(Optional.empty());
        given()
            .contentType("application/json")
            .header("Authorization", "Token " + invalidToken)
        .when()
            .get("/user")
        .then()
            .statusCode(401);
    }

    @Test
    public void should_update_current_user_profile() throws Exception {
        String newEmail = "newemail@example.com";
        String newPassword = "newpassword";
        String newUsername = "newusernamee";

        User updatedUser = new User(newEmail, newPassword, newUsername);
        updatedUser.setId(id);

        ObjectMapper mapper = new ObjectMapper();
        String param = mapper.writeValueAsString(updatedUser);

        when(userService.findByUsername(eq(newUsername))).thenReturn(Optional.empty());
        when(userService.findByEmail(eq(newEmail))).thenReturn(Optional.empty());
        when(userService.update(eq(updatedUser))).thenReturn(Optional.of(updatedUser));

        given()
            .contentType("application/json")
            .header("Authorization", "Token " + token)
            .body(param)
        .when()
            .put("/user")
        .then()
            .statusCode(200);
    }

    @Test
    public void should_get_error_if_email_exists_when_update_user_profile() throws Exception {
        String newEmail = "newemail@example.com";
        String newPassword = "updated";
        String newUsername = "newusernamee";

        User updatedUser = new User(newEmail, newPassword, newUsername);
        updatedUser.setId(id);

        ObjectMapper mapper = new ObjectMapper();
        String param = mapper.writeValueAsString(updatedUser);

        when(userService.findByEmail(eq(newEmail))).thenReturn(Optional.of(new User(newEmail, "username", "123")));
        when(userService.findByUsername(eq(newUsername))).thenReturn(Optional.empty());

        given()
            .contentType("application/json")
            .header("Authorization", "Token " + token)
            .body(param)
        .when()
            .put("/user")
            .prettyPeek()
        .then()
            .statusCode(422)
            .body("errors.email[0]", equalTo("email already exist"));
    }

    @Test
    public void should_get_401_if_not_login() throws Exception {
        given()
            .contentType("application/json")
            .body(new HashMap<String, Object>() {{
                put("user", new HashMap<String, Object>());
            }})
        .when()
            .put("/user")
            .then().statusCode(401);
    }
}
