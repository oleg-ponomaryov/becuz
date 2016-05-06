package co.becuz.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import co.becuz.domain.Role;

public class UserCreateForm {

    private String id;

    @NotEmpty
    private String action = "create";
    
    @NotEmpty
    private String email = "";

	@NotEmpty
    private String password = "";

    @NotEmpty
    private String passwordRepeated = "";

    @NotNull
    private Role role = Role.USER;

    public String getEmail() {
		return email;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}
    
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
    
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeated() {
		return passwordRepeated;
	}

	public void setPasswordRepeated(String passwordRepeated) {
		this.passwordRepeated = passwordRepeated;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	 @Override
	 public String toString() {
	 return "UserCreateForm{" +
	 "email='" + email.replaceFirst("@.+", "@***") + '\'' +
	 ", password=***" + '\'' +
	 ", passwordRepeated=***" + '\'' +
	 ", role=" + role +
	 '}';
	 }
	
}