package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RegisterController {

   @GetMapping("/register")
   public String viewHome(Model model) {
	 
      return "user/register";
   }



 
}
