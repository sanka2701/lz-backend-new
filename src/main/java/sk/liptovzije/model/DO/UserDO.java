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

	public static class Builder {
		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		private LocalDate createdDate;
		private String username;
		private String password;
		private String salt;
		private String role;

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
		public Builder salt(String salt){
			this.salt = salt;
			return this;
		}
		public Builder role(String role){
			this.role = role;
			return this;
		}
		public UserDO build(){
			return new UserDO(this);
		}
	}

	public UserDO() {
		this.role = "user";
	}

	public UserDO(String role, UserCredentialsDO credentials){
		this.role = role;
		this.setCredentials(credentials);
	}

	private UserDO(Builder builder) {
	  	this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.createdDate = builder.createdDate;
		this.salt = builder.salt;
		this.role = builder.role;
		this.setCredentials(new UserCredentialsDO(builder.username, builder.password));
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

//	public void setUsername(String username) {
//		this.username = username;
//	}

	public String getPassword() {
		return password;
	}

//	public void setPassword(String password) {
//		this.password = password;
//	}

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

	public UserCredentialsDO getCredentials() {
		return new UserCredentialsDO(username, password, salt);
	}

	public void setCredentials(UserCredentialsDO credentials) {
		this.username = credentials.getUsername();
		this.password = credentials.getPassword();
		this.salt = credentials.getSalt();
	}

	private void hashPassword() {
		
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
