package co.becuz.controllers;

import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.becuz.domain.User;
import co.becuz.dto.CreateUserDTO;
import co.becuz.forms.UserCreateForm;
import co.becuz.services.UserService;
import co.becuz.validators.UserCreateFormValidator;

@Controller
public class UserController {

    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;
    private static final Logger log = LoggerFactory
			.getLogger(UserController.class);
    
    @Autowired
    public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

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

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public String handleUserUpdateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_create";
        }
        try {
            userService.update(form);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        return "redirect:/users/all";
    }
    
    @RequestMapping("user/edit/{id}")
    public String edit(@PathVariable String id, Model model){
    	User user = userService.getUserById(id);
    	if (user == null) {
    		throw new NoSuchElementException(String.format("User=%s not found", id));
    	}
    	
    	UserCreateForm form = new UserCreateForm();
    	form.setAction("update");
    	form.setId(user.getId());
    	form.setEmail(user.getEmail());
    	form.setPassword("");
    	form.setPasswordRepeated("");
    	form.setRole(user.getRole());
    	
        model.addAttribute("form", form);
        return "user_create";
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
    		
    		return  userService.createSelf(userdto);
    		
     	}
    	catch (Exception e) {
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	
    	return null;
    }
    
    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    public @ResponseBody User updateUser(@RequestBody User user) {
    	// 0. Only can be called by USER Role, ADMIN use regular /users/ PUT (update)
    	// 1.Only allowed to update itself
    	// 2. Not allowed to changed Role (from User to Admin and back)
    	// 3. Only certain fileds can be changed (define those)
    	
    	return null;
    }

    
    
}