package co.becuz.services.currentuser;

import co.becuz.domain.CurrentUser;

public interface CurrentUserService {
	boolean canAccessUser(CurrentUser currentUser, String userId);
}
