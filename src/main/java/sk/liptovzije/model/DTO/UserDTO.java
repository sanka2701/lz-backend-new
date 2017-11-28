package sk.liptovzije.model.DTO;

import org.joda.time.LocalDate;
import sk.liptovzije.model.DO.UserDO;

public class UserDTO {
    private Long   id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private LocalDate createdDate;
    private String role;

    private String username;
    private String password;

    public static class Builder {
        private Long   id;
        private String firstName;
        private String lastName;
        private String email;
        private LocalDate createdDate;
        private String role;

        private String username;
        private String password;

        public Builder(String username, String password){
            this.username = username;
            this.password = password;
        }

        public Builder id(Long id){
            this.id = id;
            return this;
        }
        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }
        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder createdDate(LocalDate createdDate){
            this.createdDate = createdDate;
            return this;
        }
        public Builder role(String role){
            this.role = role;
            return this;
        }
        public UserDTO build(){
            return new UserDTO(this);
        }
    }

    public UserDTO() {
        this.role = "user";
    }

    private UserDTO(Builder builder) {
        this.id          = builder.id;
        this.firstName   = builder.firstName;
        this.lastName    = builder.lastName;
        this.email       = builder.email;
        this.createdDate = builder.createdDate;
        this.username    = builder.username;
        this.password    = builder.password;
        this.role        = builder.role;
    }

    public UserDTO (UserDO user) {
        this.id          = user.getId();
        this.firstName   = user.getFirstName();
        this.lastName    = user.getLastName();
        this.email       = user.getEmail();
        this.createdDate = user.getCreatedDate();
        this.role        = user.getRole();
    }

    public UserDO toDo() {
        UserDO result = new UserDO();
        result.setId(id);
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setEmail(email);
        result.setCreatedDate(createdDate);
        result.setRole(role);

        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
