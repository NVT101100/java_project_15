package com.SocialNetwork.Controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.print.attribute.standard.PrinterLocation;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.Actions;
import com.SocialNetwork.Entity.Friends;
import com.SocialNetwork.Entity.Nontifications;
import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.ActionRepository;
import com.SocialNetwork.Repository.FriendRepository;
import com.SocialNetwork.Repository.NontificationRepository;
import com.SocialNetwork.Repository.PostRepository;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Service.CalculateTime;
import com.SocialNetwork.Service.FriendService;
import com.SocialNetwork.Sheet.CommentSheet;
import com.SocialNetwork.Sheet.FriendSheet;
import com.SocialNetwork.Sheet.NonSheetCopy;
import com.SocialNetwork.Sheet.PostWithLikeSheet;

@Controller
public class ProfileController {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private NontificationRepository nonRepository;
	@Autowired
	private ActionRepository actionRepository;

	@PostMapping(value = "/user/profile/addprofile/{userId}")
	public @ResponseBody String uploadProfile(HttpServletRequest request, MultipartFile images,
			@PathVariable("userId") String id) {
		User user = repository.findById(Integer.valueOf(id)).get();
		try {
			user.setProfile(Base64.getEncoder().encodeToString(images.getBytes()));
			repository.save(user);
		} catch (IOException e) {
			e.printStackTrace();
			return "false";
		}
		createAvatarPost(id, images);
		return "true";
	}

	@GetMapping("/user/profile/{userId}")
	public ModelAndView details(@PathVariable("userId") String userId, Authentication authentication) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		CalculateTime calculateTime = new CalculateTime();
		ModelAndView model = new ModelAndView("users/profile");
		User auth = repository.findByEmail(authentication.getName());
		User user = repository.findById(Integer.valueOf(userId)).get();
		System.out.println(auth.getEmail()+" "+user.getEmail());
		boolean isFriend = false, isWaiting = false;
		boolean isAuth = true;
		boolean isYourPost = true;
		List<FriendSheet> listFriend = new ArrayList<>();
		List<PostWithLikeSheet> myPostWithLikeSheets = new ArrayList<>();
		if (auth.getUser_id() == user.getUser_id()) {
			List<Posts> postList = auth.getPosts();
			isAuth = true;
			isYourPost = true;
			for (Posts p : postList) {
				if (p != null) {
					boolean isLiked = isLiked(user.getUser_id(), p.getPost_id());
					List<CommentSheet> commentSheets = new ArrayList<>();
					List<Actions> Comments = actionRepository.findActionByType(p.getPost_id(), "comment");
					if (p.getUserPost().getUser_id() == user.getUser_id())
						for (Actions a : Comments) {
							CommentSheet comment = new CommentSheet(a.getAction_id(), a.getText(), a.getUser(),
									a.getDate(), a.getTime());
							commentSheets.add(comment);
						}
					myPostWithLikeSheets.add(new PostWithLikeSheet(p, isLiked,
							actionRepository.findActionByType(p.getPost_id(), "like").size(), commentSheets.size(),
							commentSheets, isYourPost,calculateTime.calculateTime(p.getDate(), p.getTime())));
				}
			}
			model.addObject("currentUser", auth);
			model.addObject("accessUser", auth);
			model.addObject("posts", myPostWithLikeSheets);
			listFriend = FriendService.listFriend(auth);
			model.addObject("numFriend", listFriend.size());
			if (listFriend.size() > 5)
				listFriend = listFriend.subList(0, 5);
		} else {
			List<Posts> postList = user.getPosts();
			isYourPost = false;
			for (Posts p : postList) {
				if (p != null) {
					boolean isLiked = isLiked(auth.getUser_id(), p.getPost_id());
					List<CommentSheet> commentSheets = new ArrayList<>();
					List<Actions> Comments = actionRepository.findActionByType(p.getPost_id(), "comment");
					if (p.getUserPost().getUser_id() == user.getUser_id())
						for (Actions a : Comments) {
							CommentSheet comment = new CommentSheet(a.getAction_id(), a.getText(), a.getUser(),
									a.getDate(), a.getTime());
							commentSheets.add(comment);
						}
					myPostWithLikeSheets.add(new PostWithLikeSheet(p, isLiked,
							actionRepository.findActionByType(p.getPost_id(), "like").size(), commentSheets.size(),
							commentSheets, isYourPost,calculateTime.calculateTime(p.getDate(), p.getTime())));
				}
			}
			model.addObject("posts", myPostWithLikeSheets);
			isAuth = false;
			model.addObject("currentUser", user);
			model.addObject("accessUser", auth);
			listFriend = FriendService.listFriend(user);
			model.addObject("numFriend", listFriend.size());
			if (listFriend.size() > 5)
				listFriend = listFriend.subList(0, 5);
		}
		Friends f = friendRepository.findFriend(auth.getUser_id(), Integer.valueOf(userId));
		if (f != null) {
			if (!f.isWaiting() && f.isConfirmed() && !f.isBlocked()) {
				isFriend = true;
			} else if (f.isWaiting() && !f.isConfirmed() && !f.isBlocked()) {
				isFriend = true;
				isWaiting = f.isWaiting();
			} else
				isFriend = false;
		}
		model.addObject("isAuth", isAuth);
		model.addObject("isfriend", isFriend);
		model.addObject("isWaiting", isWaiting);
		model.addObject("friends", listFriend);
		return model;
	}

	@PostMapping(value = "/user/profile/addcover/{userId}")
	public @ResponseBody String uploadCover(HttpServletRequest request, MultipartFile images,
			@PathVariable("userId") String id) {
		User user = repository.findById(Integer.valueOf(id)).get();
		try {
			user.setCover(Base64.getEncoder().encodeToString(images.getBytes()));
			repository.save(user);
		} catch (IOException e) {
			e.printStackTrace();
			return "false";
		}
		createCoverPost(id, images);
		return "true";
	}

	@PostMapping("/user/addfriend/{userId}")
	public @ResponseBody NonSheetCopy addNewFriend(@PathVariable("userId") String userId, Authentication authentication,
			@RequestBody String type) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		CalculateTime calculateTime = new CalculateTime();
		NonSheetCopy nonSheetCopy = null;
		User receiver = repository.findById(Integer.valueOf(userId)).get();
		User sender = repository.findByEmail(authentication.getName());
		Friends friends = new Friends();
		List<Friends> listFriend = new ArrayList<>();
		if (type.equals("add")) {
			Nontifications nontifications = new Nontifications();
			List<Nontifications> listNon = new ArrayList<>();
			nontifications.setReceiver(receiver);
			nontifications.setSender(sender);
			nontifications.setType("addfriend");
			nontifications.setText(" đã gửi cho bạn lời mời kết bạn");
			nontifications.setDate(date);
			nontifications.setTime(time);
			nontifications = nonRepository.save(nontifications);
			listNon.add(nontifications);
			friends.setUser1(sender);
			friends.setUser2(receiver);
			friends.setBlocked(false);
			friends.setConfirmed(false);
			friends.setWaiting(true);
			friends.setDate(date);
			listFriend.add(friends);
			receiver.setFriends2(listFriend);
			receiver.setNons2(listNon);
			sender.setFriends1(listFriend);
			repository.saveAndFlush(receiver);
			repository.saveAndFlush(sender);
			nonSheetCopy = new NonSheetCopy(nontifications,calculateTime.calculateTime(date, time),sender);
			return nonSheetCopy;
		}
		if (type.equals("cancel")) {
			Friends friend = friendRepository.findFriend(sender.getUser_id(), receiver.getUser_id());
			friendRepository.delete(friend);
			return null;
		}
		return null;
	}

	public void createAvatarPost(String id, MultipartFile images) {
		Posts changeAvatarPost = new Posts();
		User user = repository.findById(Integer.valueOf(id)).get();
		List<Posts> postList = user.getPosts();
		try {
			changeAvatarPost.setImage(Base64.getEncoder().encodeToString(images.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String statusText = user.getSex().equals("male") ? "anh ấy" : "cô ấy";
		changeAvatarPost.setStatus(user.getFullname() + " vừa cập nhật ảnh đại diện của " + statusText);
		changeAvatarPost.setUserPost(user);
		postList.add(changeAvatarPost);
		user.setPosts(postList);
		repository.saveAndFlush(user);
	}

	public void createCoverPost(String id, MultipartFile images) {
		Posts changeAvatarPost = new Posts();
		User user = repository.findById(Integer.valueOf(id)).get();
		List<Posts> postList = user.getPosts();
		try {
			changeAvatarPost.setImage(Base64.getEncoder().encodeToString(images.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String statusText = user.getSex().equals("male") ? "anh ấy" : "cô ấy";
		changeAvatarPost.setStatus(user.getFullname() + " vừa cập nhật ảnh bìa của " + statusText);
		changeAvatarPost.setUserPost(user);
		postList.add(changeAvatarPost);
		user.setPosts(postList);
		repository.saveAndFlush(user);
	}

	public boolean isLiked(int userId, int postId) {
		Actions actions = new Actions();
		actions = actionRepository.likeOfPost(userId, postId, "like");
		if (actions == null)
			return false;
		return true;
	}
}
