package com.SocialNetwork.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.UserRepository;

@Controller
public class ProfileController {

	@Autowired
	private UserRepository repository;
	private User user;
	
	@GetMapping("/user/profile")
	public String profilePage() {
		return "users/profile";
	}
	
	@PostMapping(value ="/user/profile/addprofile")
	public @ResponseBody String uploadProfile(HttpServletRequest request,Authentication auth,MultipartFile images) {
		String currentEmail = auth.getName();
		user = repository.findByEmail(currentEmail);
		try {
			user.setProfile(images.getBytes());
			repository.save(user);
			return "true";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
		
	}
	
	@GetMapping("/user/profile/{userId}")
	public String details(@PathVariable Integer userId) {
		
		return "users/profile";
	}
}
