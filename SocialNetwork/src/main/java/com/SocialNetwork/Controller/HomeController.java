package com.SocialNetwork.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.PostRepository;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Sheet.PostSheet;

@Controller
public class HomeController {

	@Autowired
	private UserRepository repository;
	private String currentEmail = null;
	private int islocked = 1, isenabled = 0;
	private User user;
	private List<User> Users = new ArrayList<>();
	private List<Posts> postList = new ArrayList<>();
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping("/user/index")
	public ModelAndView homePage(Authentication authentication,HttpServletResponse response) {
		currentEmail = authentication.getName();
		user = repository.findByEmail(currentEmail);
		islocked = user.getLocked();
		isenabled = user.getEnabled();
		if (islocked == 0 && isenabled == 1) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("users/index");
			modelAndView.addObject("currentUser",user);
			Users.add(user);
			postList = user.getPosts();
			modelAndView.addObject("friends",Users);
			modelAndView.addObject("posts", postList);
			return modelAndView;
		} else {
			return new ModelAndView("redirect:/invalid");
		}
	}
	
	
	@GetMapping("/user/displayProfile/{userId}")
	@ResponseBody
	void showFriendProfile(HttpServletResponse response,@PathVariable("userId") Integer id) {
		Optional<User> user = repository.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		try {
			response.getOutputStream().write(user.get().getProfile());
			response.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostMapping("/user/post/{userId}")
	public @ResponseBody String postStatus(@PathVariable("userId") String userId,@RequestBody PostSheet post) {
		Posts posts = new Posts();
		List<Posts> listPost = new ArrayList<Posts>();
		User userpost = repository.findById(Integer.valueOf(userId)).get();
		posts.setImage(post.getImage());
		posts.setStatus(post.getStatus());
		posts.setDate(post.getDate());
		posts.setUserPost(userpost);
		listPost.add(posts);
		userpost.setPosts(listPost);
		postRepository.save(posts);
		repository.saveAndFlush(userpost);
		return "true";
	}
	
	@GetMapping("/user/displayPostImage/{postId}")
	@ResponseBody
	void showPostImage(HttpServletResponse response,@PathVariable("postId") Integer id) {
		Posts posts = postRepository.findById(id).get();
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		try {
			response.getOutputStream().write(posts.getImage());
			response.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
