package co.becuz.services;

import java.util.Collection;
import java.util.Map;

import co.becuz.domain.User;
import co.becuz.dto.CreateUserDTO;
import co.becuz.exceptions.UserExistsException;

public interface UserService {

    User getUserById(String id);

    Collection<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

	void delete(String id);

	User save(User user);

	User update(User user);

	Map<User, Boolean> createSelf(CreateUserDTO userdto) throws UserExistsException;

	Map<String, String> swap(String id, User user);
}