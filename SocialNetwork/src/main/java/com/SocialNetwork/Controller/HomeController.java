package com.SocialNetwork.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository repository;
	private String currentEmail = null;
	private int islocked = 1,isenabled = 0;
	private User user;
	
	@GetMapping("/user/index")
	public String homePage(Authentication authentication) {
		currentEmail = authentication.getName();
		user = repository.findByEmail(currentEmail);
		islocked = user.getLocked();
		isenabled = user.getEnabled();
		if(islocked == 0 && isenabled == 1) return "users/index";
		else if (islocked == 1 && isenabled == 1) return "user/LockedAccount";
		else if (islocked == 0 && isenabled == 0) return "user/InactiveAccount";
		return "error";
	}
}
