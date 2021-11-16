package com.SocialNetwork.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;

@Controller
public class VideoController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/user/video")
	public ModelAndView videoPage(Authentication authentication) {
		ModelAndView model = new ModelAndView("users/video");
		User user = userRepository.findByEmail(authentication.getName());
		model.addObject("currentUser", user);
		return model;
	}
}
