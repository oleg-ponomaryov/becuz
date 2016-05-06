package co.becuz.controllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.becuz.domain.User;
import co.becuz.forms.UserCreateForm;
import co.becuz.services.UserService;
import co.becuz.validators.UserCreateFormValidator;

@Controller
public class UserController {

    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;

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

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String getUserCreatePage(Model model) {
        model.addAttribute("form", new UserCreateForm());
        return "user_create";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_create";
        }
        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        return "redirect:/users";
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
        return "redirect:/users";
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
    
    @RequestMapping("/users")
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
}