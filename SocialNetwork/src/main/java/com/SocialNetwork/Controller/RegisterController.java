package com.SocialNetwork.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Service.PasswordEncoder;

@Controller
@RequestMapping(path = "/")
public class RegisterController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/register")
	public String addNewUser() {

		return "user/register";
	}

	@RequestMapping(value = "/adduser",method = RequestMethod.GET)
	public String adduser(@RequestParam("fullName") String name, @RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("gender") String gender) {
		//User user = new User();
		
		/*PasswordEncoder passwordEncoder = new PasswordEncoder();
		user.setUser_password(passwordEncoder.passwordEncoder(password));
		user.setFullname(name);
		user.setSex(gender);
		user.setEmail(email);
		user.setEnabled(1);
		user.setLocked(1);
		userRepository.save(user);*/
		return "redirect:register";
	}

}