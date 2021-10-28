package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/user/index")
	public String homePage() {
		return "users/index";
	}
}
