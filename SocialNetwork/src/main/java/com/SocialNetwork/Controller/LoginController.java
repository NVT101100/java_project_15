<<<<<<< HEAD:SocialNetwork/src/main/java/com/SocialNetwork/Controller/LoginController.java
package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String loginPage() {
		return "user/login";
	}
	
	
}
=======
package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String loginPage() {
		return "users/login";
	}
	
	
}
>>>>>>> b294e1654464e6366dd5c35e19e7cb01c33c5ba5:FirstProject/MySocialNetwork/src/main/java/com/SocialNetwork/Controller/LoginController.java
