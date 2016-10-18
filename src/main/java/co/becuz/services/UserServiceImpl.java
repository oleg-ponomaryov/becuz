package co.becuz.services;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.CreateUserDTO;
import co.becuz.exceptions.UserExistsException;
import co.becuz.repositories.CollectionRepository;
import co.becuz.repositories.PhotoRepository;
import co.becuz.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private CollectionRepository collectionRepository;
    
    @Autowired
    private EncryptionService encryptionService;
    
    
	private static final Logger LOG = LoggerFactory
			.getLogger(UserServiceImpl.class);
    
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
    @Transactional
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

    	if (user.getDob() != null)
    		us.setDob(user.getDob());

    	if (user.getFirstname() != null)
    		us.setFirstname(user.getFirstname());

    	if (user.getLastname() != null)
    		us.setLastname(user.getLastname());
    	
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
        
    	if (user.getPassword()!=null)
    		us.setPasswordHash(new BCryptPasswordEncoder().encode(user.getPassword()));
    
    	if (user.getSocialPasswordHash()!=null)
    		us.setSocialPasswordHash(new BCryptPasswordEncoder().encode(user.getSocialPasswordHash()));
    	
        return userRepository.save(us);
    }

	@Override
	public Map<User, Boolean> createSelf(CreateUserDTO userdto) throws UserExistsException  {
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
		
		User user=null;
		Boolean existing = false;
		Collection<User> existingUsers = getUserByEmail(email);
		if (existingUsers!=null && existingUsers.size()>0) {
			for (User u : existingUsers) {
				if (u.getPasswordHash()==null && userdto.getUser().getSigninprovider()==null) {
					throw new UserExistsException(String.format("User with email %s already exists", email));
				}
				user = u;
				existing = true;
			}
		}
		else {
	    	user = new User();
		}

		String password = encryptionService.decrypt(userdto.getUser().getPassword(), iv);
		if (StringUtils.isEmpty(password)) {
        	throw new IllegalStateException("Password is not valid");
		}
		
    	User udto = userdto.getUser();

    	if (udto.getDob() != null)
    		user.setDob(udto.getDob());

    	if (udto.getFirstname() != null)
    		user.setFirstname(udto.getFirstname());

    	if (udto.getLastname() != null)
    		user.setLastname(udto.getLastname());
    	
    	if (udto.getPhotoUrl() != null)
    		user.setPhotoUrl(udto.getPhotoUrl());
    	
    	boolean social = false;
    	user.setSigninprovider(null);
    	if (udto.getSigninprovider()!=null) {
    		user.setSigninprovider(udto.getSigninprovider());
    		social = true;
    	}	
    		
    	if (udto.getUsername()!=null)
    		user.setUsername(udto.getUsername());
    	
    	// 1. We update password for new user regardless
    	// 2. We update password for existing user if current request is a 'signed up'
    	if (password!=null) {
	    	if (!social && user.getPasswordHash()==null) {
	    		user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
	    	}
	    	else if (social){
	    		user.setSocialPasswordHash(new BCryptPasswordEncoder().encode(password));
	    	}
    	}

    	user.setRole(Role.USER);
    	user.setEmail(email);
        
		CurrentUser userDetails = new CurrentUser(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Map<User, Boolean> ret = new HashMap<User, Boolean>();
		ret.put(userRepository.save(user), existing);
		
        return ret;
	}
	
	@Override
	@Transactional
	public Map<String, String> swap(String id, User user)  {
		Map<String, String> ret = new HashMap<String, String>();
		Collection<Photo> photos = null;
		Collection<co.becuz.domain.Collection> collections;
		try {
			// Check if user with <id> exists.
	    	User us = getUserById(id);
	    	if (us == null) {
	    		ret.put("message", String.format("User with id:%s not found",id));
	    		ret.put("status", "warning");
	    		return ret;
	    	}
	
	    	photos =  photoRepository.findAllByOwnerId(id);
	    	for (Photo p : photos) {
	    		p.setOwner(user);
	    	}
	    	photoRepository.save(photos);
	    	
	    	collections = collectionRepository.findAllByUserId(id);
	    	for (co.becuz.domain.Collection c : collections) {
	    		c.setUser(user);
	    	}
	    	collectionRepository.save(collections);
		}
		catch (Exception e) {
			LOG.error("Error during users swap", e);
    		ret.put("message", e.getMessage());
    		ret.put("status", "error");
    		return ret;
		}
    	
		ret.put("message", String.format("Assigned %d photos to user %s; assigned %d collections to user %s",photos.size(), user.getId(), collections.size(), user.getId()));
		ret.put("status", "success");
		return ret;
	}
}
