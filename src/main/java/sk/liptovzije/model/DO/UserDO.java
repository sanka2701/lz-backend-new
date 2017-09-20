package sk.liptovzije.model.DO;

import org.joda.time.LocalDate;
import sk.liptovzije.utils.LocalDatePersistenceConverter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserDO implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "create_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	private LocalDate createdDate;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "salt")
	private String salt;

	@Column(name = "role")
	private String role;

	public UserDO() {
		this.role = "user";
	}

	public UserDO(String role, UserCredentials credentials){
		this.role = role;
		this.setCredentials(credentials);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserCredentials getCredentials() {
		return new UserCredentials(username, password, salt);
	}

	public void setCredentials(UserCredentials credentials) {
		this.username = credentials.getUsername();
		this.password = credentials.getSaltedPassword();
		this.salt = credentials.getSalt();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserDO))
			return false;
		UserDO other = (UserDO) obj;
        return !(email != other.email || username != other.email);
    }

	@Override
	public String toString() {
		return new StringBuilder()
				.append("id: ").append(id)
				.append(" firstName: ").append(firstName)
				.append(" lastName: ").append(lastName)
				.append(" email: ").append(email)
				.toString();
	}
}
