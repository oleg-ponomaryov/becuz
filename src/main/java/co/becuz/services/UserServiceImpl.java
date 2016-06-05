package co.becuz.services;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.becuz.domain.User;
import co.becuz.forms.UserCreateForm;
import co.becuz.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(String id) {
        return userRepository.findOne(id);
    }

    @Override
    public Collection<User> getUserByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    @Override
    public User create(UserCreateForm form) {
    	Collection<User> users = getUserByEmail(form.getEmail());
        if (users == null || users.size()==0) {
	    	User user = new User();
	        user.setEmail(form.getEmail());
	        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
	        user.setRole(form.getRole());
	        return userRepository.save(user);
        }
        else {
        	return users.iterator().next();
        }
    }
    
    @Override
    public User update(UserCreateForm form) {
    	User user = getUserById(form.getId());
    	if (user == null) {
    		throw new NoSuchElementException(String.format("User=%s not found", form.getId()));
    	}

        user.setEmail(form.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setRole(form.getRole());
        return userRepository.save(user);
    }
    
    @Override
    public void delete(String id) {
        userRepository.delete(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public User update(User user) {
    	User us = getUserById(user.getId());
    	if (us == null) {
    		throw new NoSuchElementException(String.format("User=%s not found", user.getId()));
    	}

    	if (user.getPhotoUrl() != null)
    		us.setPhotoUrl(user.getPhotoUrl());
    	
    	if (user.getRole()!=null)
    		us.setRole(user.getRole());
    		
    	if (user.getSigninprovider()!=null)
    		us.setSigninprovider(user.getSigninprovider());
    		
    	if (user.getUsername()!=null)
    		us.setUsername(user.getUsername());
    	
    	if (user.getEmail()!=null)
    		us.setEmail(user.getEmail());
        
    	if (user.getPasswordHash()!=null)
    		us.setPasswordHash(new BCryptPasswordEncoder().encode(user.getPasswordHash()));
    	
        return userRepository.save(us);
    }
}
