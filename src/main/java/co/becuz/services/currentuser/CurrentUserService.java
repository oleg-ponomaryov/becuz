package co.becuz.services.currentuser;

import co.becuz.domain.nottables.CurrentUser;

public interface CurrentUserService {
	boolean canAccessUser(CurrentUser currentUser, String userId);
}
