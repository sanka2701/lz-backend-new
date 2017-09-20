package sk.liptovzije.model.DTO;

import org.joda.time.LocalDate;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DO.UserCredentials;

/**
 * Created by husenica on 17.9.16.
 */
public class UserDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private LocalDate createdDate;
    private String username;
    private String password;
    private String role;

    public UserDTO() {
        this.role = "user";
    }

    public UserDTO (UserDO user) {
        this.firstName  = user.getFirstName();
        this.lastName   = user.getLastName();
        this.email      = user.getEmail();
        this.createdDate= user.getCreatedDate();
        this.username   = user.getUsername();
        this.password   = user.getPassword();
        this.role       = user.getRole();
    }

    public UserDO toDo() {
        UserDO result = new UserDO();
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setEmail(email);
        result.setCreatedDate(createdDate);
        result.setRole(role);
        result.setCredentials(new UserCredentials(username, password));

        return result;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
