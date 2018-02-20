package sk.liptovzije.application.user;

import lombok.Getter;

@Getter
public class UserWithToken {
    // todo: add remaining user data except password
    private String email;
    private String username;
    private String token;

    public UserWithToken(UserData userData, String token) {
        this.email = userData.getEmail();
        this.username = userData.getUsername();
        this.token = token;
    }

}
