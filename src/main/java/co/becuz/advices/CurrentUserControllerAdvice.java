package co.becuz.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import co.becuz.domain.nottables.CurrentUser;

@ControllerAdvice
public class CurrentUserControllerAdvice {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CurrentUserControllerAdvice.class);

	@ModelAttribute("currentUser")
	public CurrentUser getCurrentUser(Authentication authentication) {
		return (authentication == null) ? null : (CurrentUser) authentication
				.getPrincipal();
	}
}