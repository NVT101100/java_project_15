package com.SocialNetwork.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController {

	@GetMapping("/user/profile")
	public String profilePage() {
		return "users/profile";
	}
	
	@PostMapping(value ="/user/profile/addprofile")
	public @ResponseBody String uploadProfile(HttpServletRequest request) {
		return "true";
	}
}
