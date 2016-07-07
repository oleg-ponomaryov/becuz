package co.becuz.services.currentuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CurrentUserServiceImpl.class);

	@Override
	public boolean canAccessUser(CurrentUser currentUser, String userId) {
		LOGGER.debug("Checking if user={} has access to user={}", currentUser,
				userId);
		return currentUser != null
				&& (currentUser.getRole() == Role.ADMIN || currentUser.getId()
						.equals(userId));
	}
}