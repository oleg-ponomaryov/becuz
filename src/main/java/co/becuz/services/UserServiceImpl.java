package co.becuz.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.NoSuchElementException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.CreateUserDTO;
import co.becuz.forms.UserCreateForm;
import co.becuz.repositories.PhotoRepository;
import co.becuz.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EncryptionService encryptionService;
    
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
    @Transactional
    public void delete(String id) {
    	//photoRepository.deleteInBatch(photoRepository.findAllByOwner(userRepository.findOne(id)));
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

	@Override
	public User createSelf(CreateUserDTO userdto)  {
		
		byte[] iv;
		try {
			iv = userdto.getIv().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported");
		}
		
		String dappId = encryptionService.decrypt(userdto.getAppId(), iv);
		if (dappId==null || !dappId.equals(encryptionService.getAppId())) {
        	throw new IllegalStateException("Application bundles ids don't match");
		}

		String email = encryptionService.decrypt(userdto.getUser().getEmail(), iv);
		if (!EmailValidator.getInstance().isValid(email)) {
        	throw new IllegalStateException("Email format is not valid");
		}
		
		Collection<User> existingUsers = getUserByEmail(email);
		if (existingUsers != null && existingUsers.size()>0) {
        	throw new IllegalStateException(String.format("User with email %s already exists", email));
		}

		String password = encryptionService.decrypt(userdto.getUser().getPassword(), iv);
		if (StringUtils.isEmpty(password)) {
        	throw new IllegalStateException("Password is not valid");
		}
		
    	User user = new User();
        user.setEmail(email);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        user.setRole(Role.USER);
        
		CurrentUser userDetails = new CurrentUser(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(authentication);

        return userRepository.save(user);
	}
}
