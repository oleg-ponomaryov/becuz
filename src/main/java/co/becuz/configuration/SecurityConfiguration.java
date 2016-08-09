package co.becuz.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.UserDTO;
import co.becuz.filters.FacebookTokenAuthenticationFilter;
import co.becuz.social.interceptor.FacebookConnectInterceptor;
import co.becuz.social.service.UserTaskService;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private Gson gson = new GsonBuilder().create();

	@Autowired
	private Environment environment;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserTaskService userTaskService;

	private static AmazonS3 s3Client;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.addFilterAfter(expiredSessionFilter(),
				SessionManagementFilter.class)
				.authorizeRequests().antMatchers("/").permitAll()
				.antMatchers("/startup/photos/**").permitAll()
				.antMatchers("/time/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/login/**").permitAll()
				.antMatchers("/imgs/**").permitAll()
				.antMatchers("/connect/**").permitAll()
				.antMatchers("/user/create/**").permitAll()
				.antMatchers("/user/update/**").hasAnyAuthority("USER")
				.antMatchers("/user/swap/**").hasAnyAuthority("USER")
				.antMatchers("/users/**").hasAuthority("ADMIN")
				.antMatchers("/photo/**").hasAnyAuthority("ADMIN", "USER")
				.antMatchers("/photos/**").hasAnyAuthority("ADMIN", "USER")
				.antMatchers("/frames/all").permitAll()
				.antMatchers("/frames/**").hasAnyAuthority("ADMIN", "USER")
				.antMatchers(HttpMethod.GET, "/collections/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/collections/photos/{id}").permitAll()
				.antMatchers("/collections/**").hasAnyAuthority("ADMIN", "USER")
				.anyRequest().fullyAuthenticated()
				.and().formLogin()
				.successHandler(successHandler())
				.failureHandler(failureHandler())
				.usernameParameter("email")
				.permitAll().and().logout().logoutUrl("/logout").permitAll()
				.deleteCookies("remember-me").logoutSuccessUrl("/").and()
				.rememberMe();
		http.csrf().disable();
		// remove this !!!!
		
		http.authorizeRequests().antMatchers("/").permitAll().and()
				.authorizeRequests().antMatchers("/console/**").permitAll();

		http.headers().frameOptions().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(
				new BCryptPasswordEncoder());
	}

	
	@Bean
    public FilterRegistrationBean facebookFilterRegistration(
    		FacebookTokenAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/facebook/token");
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
	
	
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
		AuthenticationSuccessHandlerImpl handler = new AuthenticationSuccessHandlerImpl(
				);
		handler.setAlwaysUseDefaultTargetUrl(false);
		//handler.setTargetUrlParameter("spring-security-redirect");
		return handler;
	}

	@Bean
	public AuthenticationFailureHandler failureHandler() {
		AuthenticationFailureHandlerImpl handler = new AuthenticationFailureHandlerImpl(
				);
		return handler;
	}

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication();
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
				"password", userDetailsService());
		rememberMeServices.setCookieName(environment
				.getProperty("local.cookieName"));
		rememberMeServices.setParameter("rememberMe");
		return rememberMeServices;
	}

	@Bean
	public ConnectController connectController() {
		ConnectController controller = new ConnectController(
				connectionFactoryLocator(), connectionRepository());
		controller.addInterceptor(new FacebookConnectInterceptor(
				userTaskService));
		return controller;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication == null) {
			throw new IllegalStateException(
					"Unable to get a ConnectionRepository: no user signed in");
		}
		return usersConnectionRepository().createConnectionRepository(
				authentication.getName());
	}

	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		return new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator(), textEncryptor());
	}

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new FacebookConnectionFactory(environment
				.getProperty("facebook.clientId"), environment
				.getProperty("facebook.clientSecret")));
		return registry;
	}

	@Bean
	public TextEncryptor textEncryptor() {
		String localEncryptionPassword = environment
				.getProperty("local.encryption.password");
		String localEncryptionSalt = environment
				.getProperty("local.encryption.hexSalt");
		return Encryptors.text(localEncryptionPassword, localEncryptionSalt);
	}

	@Bean
	public AWSCredentialsProvider credentials() {
		return new AWSCredentialsProviderChain(
				new InstanceProfileCredentialsProvider(),
				new EnvironmentVariableCredentialsProvider(),
				new SystemPropertiesCredentialsProvider());
	}

	public static AmazonS3 getS3Client() {
		return s3Client;
	}
	
	@Bean
	public AmazonS3 s3Client(final AWSCredentialsProvider creds,
			final Region region) {
		s3Client = region.createClient(AmazonS3Client.class, creds, null); 
		return s3Client; 
	}

	@Bean
	public Region region(final ConfigurationSettings settings) {
		return Region.getRegion(Regions.fromName(settings
				.getProperty("AWS_REGION")));
	}
	
	
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repo) {
	Connection<Facebook> connection = repo.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}

	 private Filter expiredSessionFilter() {
	        SessionManagementFilter smf = new SessionManagementFilter(new HttpSessionSecurityContextRepository());
	        smf.setInvalidSessionStrategy((request, response) -> response.setStatus(440));               
	        return smf;
	    }
	
	class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
		 @Override
         public void onAuthenticationFailure(
                 HttpServletRequest request,
                 HttpServletResponse response,
                 AuthenticationException ae) throws IOException, ServletException {
                     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 }
	}	
	
	class AuthenticationSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler {
	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
	      Authentication authentication) throws ServletException, IOException {
	        CurrentUser cuser = (CurrentUser) authentication.getPrincipal();
	        UserDTO user = new UserDTO(cuser.getUser());
	        
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().write(gson.toJson(user));
	        response.getWriter().flush();
	        response.getWriter().close();	        
	        clearAuthenticationAttributes(request);
	    }
	}
}