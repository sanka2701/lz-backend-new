package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.application.user.User;
import sk.liptovzije.application.user.UserData;
import sk.liptovzije.core.service.encrypt.IEncryptService;
import sk.liptovzije.core.service.encrypt.NaiveEncryptService;
import sk.liptovzije.core.service.jwt.IJwtService;
import sk.liptovzije.core.service.user.IUserService;
import sk.liptovzije.utils.exception.InvalidRequestException;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/users")
public class UsersApi {
    private IUserService userService;
    private IEncryptService encryptService;
    private IJwtService jwtService;

    @Autowired
    public UsersApi(IUserService userService,
                    NaiveEncryptService encryptService,
                    IJwtService jwtService) {
        this.userService = userService;
        this.encryptService = encryptService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
        checkInput(registerParam, bindingResult);

        User user = new User(
                registerParam.getEmail(),
                registerParam.getUsername(),
                encryptService.encrypt(registerParam.getPassword()));

        UserData userData = userService.save(user).map(User::toData).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.status(201).body(userResponse(userData, jwtService.toToken(user)));
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(/*@Valid*/ @RequestBody UpdateParam param, BindingResult bindingResult) {
        User user = userService.findById(param.getId()).orElseThrow(ResourceNotFoundException::new);
        user.setRole(param.getRole());
        userService.update(user);

        return ResponseEntity.ok(userResponse(user));
    }

    @GetMapping
    public ResponseEntity listUsers() {
        List<User> userList = userService.getAll();
        return ResponseEntity.ok(this.userResponse(userList));
    }

    @GetMapping("/id")
    public ResponseEntity getUserById(@RequestParam("id") long id) {
        Map user = userService.findById(id)
                .map(this::userResponse)
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam, BindingResult bindingResult) {
        Optional<User> optional = userService.findByUsername(loginParam.getUsername());
        if (optional.isPresent() && encryptService.check(loginParam.getPassword(), optional.get().getPassword())) {
            UserData userData = optional.map(User::toData).get();
            return ResponseEntity.ok(userResponse(userData, jwtService.toToken(optional.get())));
        } else {
            bindingResult.rejectValue("password", "INVALID", "err.invalidCredentials");
            throw new InvalidRequestException(bindingResult);
        }
    }

    private void checkInput(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        if (userService.findByUsername(registerParam.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "DUPLICATED", "err.duplicatedUsername");
        }

        if (registerParam.getEmail() != null &&
                !registerParam.getEmail().isEmpty() &&
                userService.findByEmail(registerParam.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "DUPLICATED", "err.duplicatedEmail");
        }

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
    }

    private Map<String, List> userResponse(User user) {
        return userResponse(Stream.of(user).collect(Collectors.toList()));
    }

    private Map<String, List> userResponse(List<User> users) {
        List<UserData> params = users.stream()
                .map(User::toData)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("users", params);
        }};
    }

    private Map<String, Object> userResponse(UserData userData, String token) {
        return new HashMap<String, Object>() {{
            put("user", userData);
            put("token", token);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class LoginParam {
    @NotBlank(message = "can't be empty")
    private String username;
    @NotBlank(message = "can't be empty")
    private String password;
}

// todo: tune parameters for registration, update and DTO

@Getter
@NoArgsConstructor
@AllArgsConstructor
class RegisterParam {
//    @Email(message = "should be an email")
    protected String email;
    @NotBlank(message = "can't be empty")
    protected String username;
//    @NotBlank(message = "can't be empty")
    protected String password;
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class UpdateParam extends RegisterParam {
    private Long id;
    private String role;
}