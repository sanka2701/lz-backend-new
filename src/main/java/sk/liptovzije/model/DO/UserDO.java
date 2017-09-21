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

	@Column(name = "role")
	private String role;

	public static class Builder {
		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		private LocalDate createdDate;
		private String role;

		public Builder(){
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
		public UserDO build(){
			return new UserDO(this);
		}
	}

	public UserDO() {
		this.role = "user";
	}

	private UserDO(Builder builder) {
	  	this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.createdDate = builder.createdDate;
		this.role = builder.role;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDO userDO = (UserDO) o;

		if (id != null ? !id.equals(userDO.id) : userDO.id != null) return false;
		if (firstName != null ? !firstName.equals(userDO.firstName) : userDO.firstName != null) return false;
		if (lastName != null ? !lastName.equals(userDO.lastName) : userDO.lastName != null) return false;
		if (email != null ? !email.equals(userDO.email) : userDO.email != null) return false;
		if (createdDate != null ? !createdDate.equals(userDO.createdDate) : userDO.createdDate != null) return false;
		return role != null ? role.equals(userDO.role) : userDO.role == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
		result = 31 * result + (role != null ? role.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserDO{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", createdDate=" + createdDate +
				", role='" + role + '\'' +
				'}';
	}
}
