package co.becuz.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.CreateUserDTO;
import co.becuz.exceptions.UserExistsException;
import co.becuz.services.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory
			.getLogger(UserController.class);
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

   // @InitBinder("form")
   // public void initBinder(WebDataBinder binder) {
   //     binder.addValidators(userCreateFormValidator);
   // }

    @RequestMapping("/user/{id}")
    public String getUserPage(@PathVariable String id, Model model) {
    	
    	User user = userService.getUserById(id);
    	if (user != null) {
    		model.addAttribute("user", userService.getUserById(id));
    		return "user";
    	}
    	else {
    		throw new NoSuchElementException(String.format("User=%s not found", id));
    	}
    }
    
    @RequestMapping("/users/all")
    public String getUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    
    @RequestMapping("/user/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
    	
    	User user = userService.getUserById(id);
    	if (user == null) {
    		throw new NoSuchElementException(String.format("User=%s not found", id));
    	}
    	userService.delete(id);
        return "users";
    }
    
    /*** Pure RESR API ***/
    
    /*** For large number there  will be paging ***/
    @RequestMapping(value = "/users",method=RequestMethod.GET)
    public @ResponseBody Collection<User> getAllUsers(Model model) {
        return userService.getAllUsers();
    }
    
    @RequestMapping(value = "/users",method=RequestMethod.POST)
    public @ResponseBody User create(@RequestBody User user) {
    	
        if (user.getPassword()!=null && !user.getPassword().isEmpty()) {
        	user.setPasswordHash(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
      return userService.save(user);
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="/users/{id}")
    public @ResponseBody void delete(@PathVariable String id) {
    	userService.delete(id);
    }
    
    @RequestMapping(method=RequestMethod.PUT, value="/users/{id}")
    public @ResponseBody User update(@PathVariable String id, @RequestBody User user) {
      if (user.getPassword()!=null && !user.getPassword().isEmpty()) {
          user.setPasswordHash(new BCryptPasswordEncoder().encode(user.getPassword()));
      }
    	
      return userService.update(user);
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/users/{id}")
    public @ResponseBody User getUser(@PathVariable String id) {
    	
    	User user = userService.getUserById(id);
    	if (user != null) {
    		return user;
    	}
    	else {
    		throw new NoSuchElementException(String.format("User=%s not found", id));
    	}
    }

    @RequestMapping(method=RequestMethod.GET, value="/user/email/{email_encoded:.+}")
    public @ResponseBody User getUserByEmail(@PathVariable String email_encoded) throws UnsupportedEncodingException {
    	
		String	email = URLDecoder.decode(email_encoded, "UTF-8");
    	
    	Collection<User> users = userService.getUserByEmail(email);
    	if (users != null && !users.isEmpty()) {
    		return users.iterator().next();
    	}
    	else {
    		throw new NoSuchElementException(String.format("User=%s not found", email));
    	}
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/time")
    public @ResponseBody String getCurrentTime() {
    	return Long.toString(new Date().getTime());
    }
    
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public @ResponseBody User createUser(@RequestBody CreateUserDTO userdto, HttpServletResponse response) {
    	
    	User user = null;
    	try {
    		user = userdto.getUser();
    		String appId = userdto.getAppId();
    		String iv = userdto.getIv();
    		if (user==null || StringUtils.isEmpty(user.getEmail())  || StringUtils.isEmpty(appId) || 
    				StringUtils.isEmpty(iv) || StringUtils.isEmpty(user.getPassword())) {
    			throw new NoSuchElementException ("Bad request");	
    		}
        	Map<User, Boolean> map = userService.createSelf(userdto);
        	
        	User u = map.keySet().iterator().next();
        	if (map.values().iterator().next()) {
        		response.setStatus(HttpServletResponse.SC_CONFLICT);
        	}
    		return  u;
     	}
    	catch (UserExistsException e1) {
    		response.setStatus(HttpServletResponse.SC_CONFLICT);
    	}
    	catch (Exception e) {
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	
    	return null;
    }
    
    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    public @ResponseBody User updateSelf(@RequestBody @Valid User user, @ModelAttribute CurrentUser currentUser, BindingResult errors, HttpServletResponse response) {
    	if (errors.hasErrors()) {
    		log.error("User validation errors");
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	else if (!user.getId().equals(currentUser.getUser().getId())) {
    		log.warn("Attempt to modify not self");
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	else {
	    	user.setRole(Role.USER);
	        return userService.update(user);
    	}
    	return null;
    }

    @RequestMapping(value = "/user/swap", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> swapUsers(@RequestBody String id, @ModelAttribute CurrentUser currentUser, HttpServletResponse response) {
    	if (StringUtils.isEmpty(id)) {
    		log.error("Not found expected user id");
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	else if (id.equals(currentUser.getUser().getId())) {
    		log.error("Request for the same user");
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	else {
    		Map<String, String> resp = userService.swap(id, currentUser.getUser());
    		if (resp.get("status").equals("warning")) {
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    		}
    		else if (resp.get("status").equals("error")) {
        		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		}
	        return resp;
    	}
    	return null;
    }
}