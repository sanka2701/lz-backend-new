package sk.liptovzije.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sk.liptovzije.TestUtils;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserDTO;
import sk.liptovzije.service.IAuthenticationService;
import sk.liptovzije.service.IJwtService;
import sk.liptovzije.service.IStorageService;
import sk.liptovzije.service.IUserService;
import sk.liptovzije.service.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private IAuthenticationService authService;

    @MockBean
    private IJwtService kwtService;


    @MockBean
    private IStorageService storageService;

//    @Test
//    public void contextLoads() throws Exception {
//        assertThat(controller).isNotNull();
//    }

    @Test
    public void shouldReturnErrorWhenUsernameIsInUse() throws Exception {
//        UserDTO dto = new UserDTO.Builder("Sanka", "heslo")
//                .email("sanka@lz.com")
//                .firstName("Johny")
//                .lastName("B Goode")
//                .role("admin")
//                .build();
//
//        UserDO udo = new UserDO.Builder("Sanka", "heslo")
//                .id(1L)
//                .email("sanka@lz.com")
//                .firstName("Johny")
//                .lastName("B Goode")
//                .role("admin")
//                .build();
//
//        when(userService.getByUsername(dto.getUsername())).thenReturn(udo);
//
//        this.mockMvc.perform(post("/user/create")
//                .contentType(TestUtils.APPLICATION_JSON_UTF8)
//                .content(TestUtils.convertObjectToJsonBytes(dto))
//        )
//                .andDo(print())
//                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(content().string(containsString("status.usernameInUse")));
    }

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        this.mockMvc.perform(get("/test")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World")));
    }
}
