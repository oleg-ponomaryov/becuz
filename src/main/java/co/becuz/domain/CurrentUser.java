package co.becuz.domain;

import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends
		org.springframework.security.core.userdetails.User {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7606201012662695478L;
	private User user;

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