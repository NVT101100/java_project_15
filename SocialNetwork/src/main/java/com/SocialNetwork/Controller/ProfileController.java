package com.SocialNetwork.Controller;

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
	
	@PostMapping("/user/profile/upload")
	@ResponseBody
	public String uploadProfile() {
		return "true";
	}
}
