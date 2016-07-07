package co.becuz.services.currentuser;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.becuz.domain.User;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.services.UserService;

@Service
public class CurrentUserDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CurrentUserDetailsService.class);

	@Autowired
	private UserService userService;

	@Override
	public CurrentUser loadUserByUsername(String email)
			throws UsernameNotFoundException {
		LOGGER.debug("Authenticating user with email={}",
				email.replaceFirst("@.*", "@***"));
		Collection<User> users = userService.getUserByEmail(email);
		if (users.size() == 0) {
			LOGGER.error("Could not find user with email={}",
					email.replaceFirst("@.*", "@***"));
			throw new UsernameNotFoundException(String.format(
					"User with email=%s was not found", email)); 	
		}
		Iterator<User> user = users.iterator();
		return new CurrentUser(user.next());
	}
}