package com.SocialNetwork.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.PostRepository;
import com.SocialNetwork.Repository.UserRepository;

@Controller
public class ProfileController {

	@Autowired
	private UserRepository repository;
	private PostRepository postRepository;
	
	@GetMapping("/user/profile")
	public String profilePage() {
		return "users/profile";
	}
	
	@PostMapping(value ="/user/profile/addprofile/{userId}")
	public @ResponseBody String uploadProfile(HttpServletRequest request,MultipartFile images,@PathVariable("userId") String id) {
		User user = repository.findById(Integer.valueOf(id)).get();
			try {
				user.setProfile(Base64.getEncoder().encodeToString(images.getBytes()));
				repository.save(user);
			} catch (IOException e) {
				e.printStackTrace();
				return "false";
			}
		createAvatarPost(id,images);
		return "true";
	}
		
	
	@GetMapping("/user/profile/{userId}")
	public ModelAndView details(@PathVariable Integer userId) {
		ModelAndView model = new ModelAndView();
		model.setViewName("users/profile");
		model.addObject("currentUser", repository.findById(userId).get());
		return model;
	}
	
	@PostMapping(value ="/user/profile/addcover/{userId}")
	public @ResponseBody String uploadCover(HttpServletRequest request,MultipartFile images,@PathVariable("userId") String id) {
		User user = repository.findById(Integer.valueOf(id)).get();
			try {
				user.setCover(Base64.getEncoder().encodeToString(images.getBytes()));
				repository.save(user);
			} catch (IOException e) {
				e.printStackTrace();
				return "false";
			}
		createCoverPost(id,images);
		return "true";
	}
	
	public void createAvatarPost(String id,MultipartFile images) {
		Posts changeAvatarPost = new Posts();
		User user = repository.findById(Integer.valueOf(id)).get();
		List<Posts> postList = user.getPosts();
		try {
			changeAvatarPost.setImage(Base64.getEncoder().encodeToString(images.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String statusText = user.getSex().equals("male")?"anh ấy":"cô ấy";
		changeAvatarPost.setStatus(user.getFullname()+" vừa cập nhật ảnh đại diện của "+statusText);
		changeAvatarPost.setUserPost(user);
		postList.add(changeAvatarPost);
		user.setPosts(postList);
		repository.saveAndFlush(user);
	}
	
	public void createCoverPost(String id,MultipartFile images) {
		Posts changeAvatarPost = new Posts();
		User user = repository.findById(Integer.valueOf(id)).get();
		List<Posts> postList = user.getPosts();
		try {
			changeAvatarPost.setImage(Base64.getEncoder().encodeToString(images.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String statusText = user.getSex().equals("male")?"anh ấy":"cô ấy";
		changeAvatarPost.setStatus(user.getFullname()+" vừa cập nhật ảnh bìa của "+statusText);
		changeAvatarPost.setUserPost(user);
		postList.add(changeAvatarPost);
		user.setPosts(postList);
		repository.saveAndFlush(user);
	}
}
