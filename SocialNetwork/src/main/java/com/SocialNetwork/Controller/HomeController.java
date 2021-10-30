package com.SocialNetwork.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;

@Controller
public class HomeController {

	@Autowired
	private UserRepository repository;
	private String currentEmail = null;
	private String currentUserGender;
	private int islocked = 1, isenabled = 0;
	private User user;

	@GetMapping("/user/index")
	public ModelAndView homePage(Authentication authentication) {
		
		currentEmail = authentication.getName();
		user = repository.findByEmail(currentEmail);
		islocked = user.getLocked();
		isenabled = user.getEnabled();
		currentUserGender = user.getSex();
		if (islocked == 0 && isenabled == 1) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("users/index");
			if (user.getProfile() == null) {
				if (currentUserGender.equals("male")) {
					modelAndView.addObject("defaultAvatar", "default-avatar-boy.jpg");
				} else if (currentUserGender.equals("female")) {
					modelAndView.addObject("defaultAvatar", "default-avatar-girl.jpg");
				} else {
					modelAndView.addObject("defaultAvatar", "default-avatar-boy.jpg");
				}
			}
			modelAndView.addObject("fullname", user.getFullname());
			return modelAndView;
		} else {
			return new ModelAndView("redirect:/invalid");
		}
	}
}
