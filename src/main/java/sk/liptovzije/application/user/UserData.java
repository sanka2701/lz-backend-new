package sk.liptovzije.application.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserData {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate createdDate;
    private String role;
}
