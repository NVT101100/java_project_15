package com.SocialNetwork.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MessageController {
	
	@GetMapping("/user/message")
	public String messagePage(Authentication authentication) {
		return "users/mess";
	}
}
