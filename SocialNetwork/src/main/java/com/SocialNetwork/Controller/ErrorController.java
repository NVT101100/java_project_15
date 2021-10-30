package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	@GetMapping("/error")
	public String errorPage() {
		return "error";
	}
	
	@GetMapping("/invalid")
	public String invalidAccount() {
		return "users/invalidAccount";
	}
}
