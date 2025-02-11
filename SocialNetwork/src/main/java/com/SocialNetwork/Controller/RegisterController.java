package com.SocialNetwork.Controller;

import java.sql.Date;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Service.GetDefaultProfileImage;
import com.SocialNetwork.Service.PasswordEncoder;

@Controller
public class RegisterController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	ResourceLoader resourceLoader;
	private GetDefaultProfileImage profileImage = new GetDefaultProfileImage();

	@GetMapping("/register")
	public String addNewUser() {

		return "users/register";
	}
	@CrossOrigin(origins = "http://localhost:8080/SocialNetwork")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView adduser(@RequestParam("fullName") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("gender") String gender) {
		Date date = new Date(System.currentTimeMillis());
		User user = new User();
		ModelAndView model = new ModelAndView();
		PasswordEncoder passwordEncoder = new PasswordEncoder();
		user.setUser_password(passwordEncoder.passwordEncoder(password));
		user.setFullname(name);
		user.setSex(gender);
		user.setEmail(email);
		user.setEnabled(0);
		user.setLocked(0);
		user.setDate(date);
		user.setRole("user");
		user.setProfile(null);
		user.setProfile(profileImage.GetProfileImageBydefault(gender));
		user.setCover(profileImage.setDefaultCover());
		if (userRepository.findByEmail(user.getEmail()) == null) {
			userRepository.save(user);
			model.setViewName("users/login");
			model.addObject("isSuccess", "block");
			return model;
		}
		else {
			model.setViewName("users/register");
			model.addObject("warning", "Email đã được sử dụng. Vui lòng sử dụng email khác!");
			model.addObject("fullName",user.getFullname());
			return model;
		}
	}

}