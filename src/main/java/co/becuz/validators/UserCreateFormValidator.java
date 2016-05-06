package co.becuz.validators;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import co.becuz.domain.User;
import co.becuz.forms.UserCreateForm;
import co.becuz.services.UserService;

@Component
public class UserCreateFormValidator implements Validator {


    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        validatePasswords(errors, form);
        validateEmail(errors, form);
    }

    private void validatePasswords(Errors errors, UserCreateForm form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("password.no_match", "Passwords do not match");
        }
    }

    private void validateEmail(Errors errors, UserCreateForm form) {

    	Collection<User> users = userService.getUserByEmail(form.getEmail());
    	if (form.getAction().equals("create")) {
	        if (users.size()>0) {
	            errors.reject("email.exists", "User with this email already exists");
	            return;
	        }
    	} else {
    		for (User u : users) {
    			if (!u.getId().equals(form.getId())) {
    	            errors.reject("email.exists", "User with this email already exists");
    	            return;
    			}
    		}
    	}
    }
}
