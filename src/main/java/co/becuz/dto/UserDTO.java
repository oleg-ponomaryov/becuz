package co.becuz.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.social.SocialMediaTypes;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = -2913377635607885687L;

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private Role role;

	@Getter
	@Setter
	private SocialMediaTypes signinprovider;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String firstname;

	@Getter
	@Setter
	private String lastname;
	
	@Getter
	@Setter
	private Date dob;
	
	@Getter
	@Setter
	private String photoUrl;

	@Getter
	@Setter
	private Date created;

	@Getter
	@Setter
	private Date updated;

	public UserDTO(User user) {
		this.setCreated(user.getCreated());
		this.setDob(user.getDob());
		this.setEmail(user.getEmail());
		this.setFirstname(user.getFirstname());
		this.setId(user.getId());
		this.setLastname(user.getLastname());
		this.setPhotoUrl(user.getPhotoUrl());
		this.setRole(user.getRole());
		this.setSigninprovider(user.getSigninprovider());
		this.setUpdated(user.getUpdated());
		this.setUsername(user.getUsername());
	}
}
