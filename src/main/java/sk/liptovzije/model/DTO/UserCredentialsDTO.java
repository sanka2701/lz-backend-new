package sk.liptovzije.model.DTO;

import sk.liptovzije.model.DO.UserCredentialsDO;

public class UserCredentialsDTO {
    private String username;
    private String password;

    public UserCredentialsDTO() {}

    public UserCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentialsDO toDo(Long userId, String salt) {
        return new UserCredentialsDO(userId, this.username, this.password, salt);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
