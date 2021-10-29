<<<<<<< HEAD:SocialNetwork/src/main/java/com/SocialNetwork/Controller/HomeController.java
package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/index")
	public String homePage() {
		return "user/index";
	}
}
=======
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
>>>>>>> b294e1654464e6366dd5c35e19e7cb01c33c5ba5:FirstProject/MySocialNetwork/src/main/java/com/SocialNetwork/Controller/RegisterController.java
