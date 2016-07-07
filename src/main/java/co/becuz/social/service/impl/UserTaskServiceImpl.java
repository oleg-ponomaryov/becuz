package co.becuz.social.service.impl;

import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;

import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.repositories.UserRepository;
import co.becuz.social.SocialMediaTypes;
import co.becuz.social.dto.SocialDTO;
import co.becuz.social.service.UserTaskService;

@Service
public class UserTaskServiceImpl implements UserTaskService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConnectionRepository connectionRepository;
			
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserTaskServiceImpl.class);

	
	@Override
	public void loginOrCreateFacebookUser(SocialDTO dto) {

		User user = this.userRepository.findByUsernameAndSigninprovider(dto.getId(), SocialMediaTypes.FACEBOOK);

		if(user == null){
			LOGGER.debug("Authenticating user with email={}",
					dto.getEmail().replaceFirst("@.*", "@***"));

			Collection<User> users = this.userRepository.findAllByEmail(dto.getEmail());
			if (users==null || users.size()==0) {
				user = new User();
			}
			else {
				user = users.iterator().next();
			}
			
			String facebookUserId = dto.getDisplayName();

			user.setSigninprovider(SocialMediaTypes.FACEBOOK);
			user.setRole(Role.USER);
			user.setUsername(facebookUserId);
			if (user.getPasswordHash()==null) {
				user.setPasswordHash(UUID.randomUUID().toString());
			}
			user.setEmail(dto.getEmail());
			user.setPhotoUrl(dto.getImageURL());
			user = this.userRepository.save(user);
		}

		CurrentUser userDetails = new CurrentUser(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}