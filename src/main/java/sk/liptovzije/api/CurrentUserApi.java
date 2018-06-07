package sk.liptovzije.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.liptovzije.application.user.User;
import sk.liptovzije.application.user.UserData;
import sk.liptovzije.core.service.user.IUserService;
import sk.liptovzije.utils.exception.InvalidRequestException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class CurrentUserApi {
    private IUserService userService;

    @Autowired
    public CurrentUserApi(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity currentUser(@AuthenticationPrincipal User currentUser,
                                      @RequestHeader(value = "Authorization") String authorization) {
        UserData userData = currentUser.toData();
        return ResponseEntity.ok(userResponse(userData, authorization.split(" ")[1]));
    }

    @PutMapping
    public ResponseEntity updateProfile(@AuthenticationPrincipal User currentUser,
                                        @RequestHeader("Authorization") String token,
                                        @Valid @RequestBody User updateUserParam,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        checkUniquenessOfUsernameAndEmail(currentUser, updateUserParam, bindingResult);

        UserData userData = userService.update(updateUserParam).map(User::toData).get();
        return ResponseEntity.ok(userResponse(userData, token.split(" ")[1]));
    }

    private void checkUniquenessOfUsernameAndEmail(User currentUser, User updatedUser, BindingResult bindingResult) {
        if (!"".equals(updatedUser.getUsername())) {
            Optional<User> byUsername = userService.findByUsername(updatedUser.getUsername());
            if (byUsername.isPresent() && !byUsername.get().equals(currentUser)) {
                bindingResult.rejectValue("username", "DUPLICATED", "username already exist");
            }
        }

        if (!"".equals(updatedUser.getEmail())) {
            Optional<User> byEmail = userService.findByEmail(updatedUser.getEmail());
            if (byEmail.isPresent() && !byEmail.get().equals(currentUser)) {
                bindingResult.rejectValue("email", "DUPLICATED", "email already exist");
            }
        }

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
    }

    private Map<String, Object> userResponse(UserData userData, String token) {
        return new HashMap<String, Object>() {{
            put("user", userData);
            put("token", token);
        }};
    }
}
