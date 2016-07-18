package co.becuz.services;

import java.util.Collection;

import co.becuz.domain.User;
import co.becuz.dto.CreateUserDTO;
import co.becuz.forms.UserCreateForm;

public interface UserService {

    User getUserById(String id);

    Collection<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

	User update(UserCreateForm form);

	void delete(String id);

	User save(User user);

	User update(User user);

	User createSelf(CreateUserDTO userdto);

}