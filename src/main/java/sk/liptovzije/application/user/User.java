package sk.liptovzije.application.user;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.joda.time.LocalDate;
import sk.liptovzije.utils.LocalDatePersistenceConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate createdDate;

    @Column(name = "role")
    private String role;

    // todo: remove, just for offline testing purposes
    private static AtomicInteger idGenerator=new AtomicInteger();

    public User(String email, String username, String password) {
        this.id = (long) idGenerator.incrementAndGet();
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = Roles.ROLE_USER;
    }

    public UserData toData() {
        UserData data = new UserData();
        data.setId(id);
        data.setUsername(username);
        data.setEmail(email);
        data.setFirstName(firstName);
        data.setLastName(lastName);
        data.setCreatedDate(createdDate);
        data.setRole(role);

        return data;
    }
}
