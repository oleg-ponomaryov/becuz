package co.becuz.domain;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends
		org.springframework.security.core.userdetails.User {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7606201012662695479L;
	private User user;

	public CurrentUser(String email, String password, List<GrantedAuthority> authorities) {
		super(email, password, authorities);
	}

	public CurrentUser(User user) {
		super(user.getEmail(), user.getPasswordHash(), AuthorityUtils
				.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getId() {
		return user.getId();
	}

	public Role getRole() {
		return user.getRole();
	}

	@Override
	public String toString() {
		return "CurrentUser{" + "user=" + user + "} " + super.toString();
	}
}