package co.becuz.configuration;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import co.becuz.domain.nottables.CurrentUser;

public class CustomAuthenticationProvider extends  DaoAuthenticationProvider {
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		Object salt = null;

		if (getSaltSource() != null) {
			salt = getSaltSource().getSalt(userDetails);
		}

		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!getPasswordEncoder().isPasswordValid(userDetails.getPassword(),
				presentedPassword, salt)) {
			CurrentUser current = (CurrentUser) userDetails;
			if (current==null || current.getUser()==null || current.getUser().getSocialPasswordHash()==null) {
				logger.debug("Authentication failed: social password not found while normal password authentication failed");
				
				throw new BadCredentialsException(messages.getMessage(
						"AbstractUserDetailsAuthenticationProvider.badCredentials",
						"Social password not found while normal password authentication failed"));
			}
			
			String hash = current.getUser().getSocialPasswordHash();
			if (!getPasswordEncoder().isPasswordValid(hash, presentedPassword, salt)) {
				logger.debug("Authentication failed: password does not match stored value");
	
				throw new BadCredentialsException(messages.getMessage(
						"AbstractUserDetailsAuthenticationProvider.badCredentials",
						"Bad credentials"));
			}
		}
	}
}
