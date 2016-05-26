package co.becuz.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.becuz.domain.CurrentUser;
import co.becuz.domain.Role;
import co.becuz.domain.User;
import co.becuz.repositories.UserRepository;
import co.becuz.social.SocialMediaTypes;

@Component
public class FacebookTokenAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private Environment environment;

	@Autowired
	private UserRepository userRepository;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FacebookTokenAuthenticationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//try {
			String xAuthtoken = request.getHeader("X-FB-Authorization");

			AccessGrant accessGrant = new AccessGrant(xAuthtoken);
			FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
					environment.getProperty("spring.social.facebook.appId"),
					environment.getProperty("spring.social.facebook.appSecret"));
			
			Connection<Facebook> connection = facebookConnectionFactory
					.createConnection(accessGrant);
			UserProfile userProfile = connection.fetchUserProfile();
			
			User user = this.userRepository.findByUsernameAndSigninprovider(
					userProfile.getUsername(), SocialMediaTypes.FACEBOOK);

			if (user == null) {
				LOGGER.debug("Authenticating user with email={}", userProfile
						.getEmail().replaceFirst("@.*", "@***"));

				Collection<User> users = this.userRepository
						.findAllByEmail(userProfile.getEmail());
				if (users == null || users.size() == 0) {
					user = new User();
				} else {
					user = users.iterator().next();
				}

				String facebookUserId = userProfile.getUsername();

				user.setSigninprovider(SocialMediaTypes.FACEBOOK);
				user.setRole(Role.USER);
				user.setUsername(facebookUserId);
				if (user.getPasswordHash() == null) {
					user.setPasswordHash(UUID.randomUUID().toString());
				}
				user.setEmail(userProfile.getEmail());
				user.setImageUrl(connection.getImageUrl());
				user = this.userRepository.save(user);
			}
			
			CurrentUser userDetails = new CurrentUser(user);
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());

			SecurityContextHolder.clearContext();
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);

			LOGGER.info("Assigned facebook user:"+userDetails.toString());
		//} catch (Exception e) {
		//	SecurityContextHolder.clearContext();
		//	throw new SecurityException();
		//}
		Cookie c = new Cookie("Success", "true");	
		response.addCookie(c);
		//.addHeader("Success", "true");  //.setHeader("Success", "true");	
		filterChain.doFilter(request, response);
	}
}
