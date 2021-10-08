package com.ryan.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ryan.authentication.models.User;
import com.ryan.authentication.services.UserService;
import com.ryan.authentication.validations.UserValidator;

// imports removed for brevity
@Controller
public class UserController {
	private final UserService userService;
	private final UserValidator userValidator;
    
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }
    
    @RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "/accounts/registrationPage.jsp";
    }
    
    @RequestMapping("/login")
    public String login() {
        return "/accounts/loginPage.jsp";
    }
    
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
    	userValidator.validate(user, result);
    	if(result.hasErrors()) {
    		return "/accounts/registrationPage.jsp";
    	}
    	else {
    		userService.registerUser(user);
    		session.setAttribute("user", user.getId());
    		
    		return "redirect:/dashboard";
    	}
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        boolean isAuth = userService.authenticateUser(email, password);
        if(isAuth) {
        	User loggedInUser = userService.findByEmail(email);
        	session.setAttribute("user", loggedInUser.getId());
        	return "redirect:/dashboard";
        	
        }
        else {
        	redirectAttributes.addFlashAttribute("error", "Invalid credentials.");
        	return "redirect:/login";
        }
    }
    
    @RequestMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
    	Object userId = session.getAttribute("user");
    	Long userIdNowLong = (Long) userId;
    	model.addAttribute("user", this.userService.findUserById(userIdNowLong));
        return "/accounts/dashboard.jsp";
    }
    
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

