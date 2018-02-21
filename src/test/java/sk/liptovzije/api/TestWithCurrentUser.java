package sk.liptovzije.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.jwt.IJwtService;
import sk.liptovzije.core.service.user.IUserService;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
abstract class TestWithCurrentUser {
    @MockBean
    protected IUserService userService;

    @MockBean
    protected IJwtService jwtService;

    protected Long id;
    protected User user;
    protected String token;
    protected String email;
    protected String username;

    protected void userFixture() {
        id = 0L;
        email = "john@jacob.com";
        username = "johnjacob";

        user = new User(email, username, "123");
        user.setId(id);
        when(userService.findByUsername(any())).thenReturn(Optional.empty());
        when(userService.findByUsername(eq(username))).thenReturn(Optional.of(user));
        when(userService.findById(any())).thenReturn(Optional.empty());
        when(userService.findById(eq(user.getId()))).thenReturn(Optional.of(user));

        token = "token";
        when(jwtService.getSubFromToken(eq(token))).thenReturn(Optional.of(user.getId().toString()));
    }

    @Before
    public void setUp() throws Exception {
        userFixture();
    }
}
