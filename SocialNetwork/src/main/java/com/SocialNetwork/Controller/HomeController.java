package com.SocialNetwork.Controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.SocialNetwork.Entity.Friends;
import com.SocialNetwork.Entity.Actions;
import com.SocialNetwork.Entity.Nontifications;
import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.FriendRepository;
import com.SocialNetwork.Repository.LoggedInCount;
import com.SocialNetwork.Repository.ActionRepository;
import com.SocialNetwork.Repository.NontificationRepository;
import com.SocialNetwork.Repository.PostRepository;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Service.CalculateTime;
import com.SocialNetwork.Sheet.CommentSheet;
import com.SocialNetwork.Sheet.FriendSheet;
import com.SocialNetwork.Sheet.NonSheetCopy;
import com.SocialNetwork.Sheet.NontificationSheet;
import com.SocialNetwork.Sheet.PostWithLikeSheet;

@Controller
public class HomeController {

	@Autowired
	private UserRepository repository;
	private String currentEmail = null;
	private int islocked = 1, isenabled = 0;
	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private ActionRepository actionRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private NontificationRepository nonRepository;
	@Autowired
	private LoggedInCount loggedIn;

	@GetMapping("/user/index")
	public ModelAndView homePage(Authentication authentication, HttpServletResponse response) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		com.SocialNetwork.Entity.LoggedInCount todayCount = new com.SocialNetwork.Entity.LoggedInCount();
		todayCount.setTime(time);
		todayCount.setDate(date);
		todayCount.setEmail(authentication.getName());
		loggedIn.save(todayCount);
		CalculateTime calTime = new CalculateTime();
		currentEmail = authentication.getName();
		User user = repository.findByEmail(currentEmail);
		islocked = user.getLocked();
		isenabled = user.getEnabled();
		boolean isYourPost = false;
		if (islocked == 0 && isenabled == 1) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("users/index");
			modelAndView.addObject("currentUser", user);
			List<FriendSheet> listFriend = new ArrayList<FriendSheet>();
			List<Nontifications> listNon = new ArrayList<>();
			List<NontificationSheet> listNewRequest = new ArrayList<>();
			List<NontificationSheet> listLikeAndComment = new ArrayList<>();
			NontificationSheet nonSheet;
			FriendSheet sheet;
			List<PostWithLikeSheet> myPostWithLikeSheets = new ArrayList<>();
			List<Posts> postList = new ArrayList<>();
			List<Friends> listOnDB1 = user.getFriends1();
			List<Friends> listOnDB2 = user.getFriends2();
			for (Friends f : listOnDB1) {
				if (!f.isBlocked() && !f.isWaiting() && f.isConfirmed()) {
					sheet = new FriendSheet(f.getUser1().getUser_id(), f.getUser1().getFullname(),
							f.getUser1().getProfile());
					listFriend.add(sheet);
					postList.addAll(f.getUser1().getPosts());
				}
			}
			for (Friends f : listOnDB2) {
				if (!f.isBlocked() && !f.isWaiting() && f.isConfirmed()) {
					sheet = new FriendSheet(f.getUser2().getUser_id(), f.getUser2().getFullname(),
							f.getUser2().getProfile());
					listFriend.add(sheet);
					postList.addAll(f.getUser2().getPosts());
				}
			}
			postList.addAll(user.getPosts());
			for (Posts p : postList) {
				if (p != null) {
					boolean isLiked = isLiked(user.getUser_id(), p.getPost_id());
					List<CommentSheet> commentSheets = new ArrayList<>();
					List<Actions> Comments = actionRepository.findActionByType(p.getPost_id(), "comment");
					if (p.getUserPost().getUser_id() == user.getUser_id())
					{
						isYourPost = true;
						//listNon.addAll(p.getNontifications());
					}
					for (Actions a : Comments) {
						CommentSheet comment = new CommentSheet(a.getAction_id(), a.getText(), a.getUser(), a.getDate(),
								a.getTime());
						commentSheets.add(comment);
					}
					myPostWithLikeSheets.add(new PostWithLikeSheet(p, isLiked,
							actionRepository.findActionByType(p.getPost_id(), "like").size(), commentSheets.size(),
							commentSheets, isYourPost, calTime.calculateTime(p.getDate(), p.getTime())));
				}
			}
			listNon = user.getNons2();
			if (!listNon.isEmpty()) {
				for (Nontifications non : listNon) {
					if (non.getType().equals("addfriend")) {
						nonSheet = new NontificationSheet(non, calTime.calculateTime(non.getDate(), non.getTime()));
						listNewRequest.add(nonSheet);
					} else if (non.getType().equals("likepost") || non.getType().equals("commentpost")
							|| non.getType().equals("acceptfriend")) {
						nonSheet = new NontificationSheet(non, calTime.calculateTime(non.getDate(), non.getTime()));
						listLikeAndComment.add(nonSheet);
					}
				}
			}
			int nonhasntseen = nonRepository.hasntSeen(user.getUser_id()).size();
			modelAndView.addObject("nonHasntSeen", nonhasntseen);
			modelAndView.addObject("friends", listFriend);
			modelAndView.addObject("posts", myPostWithLikeSheets);
			modelAndView.addObject("nontifications", listNewRequest);
			modelAndView.addObject("notifies", listLikeAndComment);
			return modelAndView;
		} else {
			return new ModelAndView("redirect:/invalid");
		}
	}

	@PostMapping("user/search/{userId}")
	public @ResponseBody List<FriendSheet> searchFriend(@PathVariable("userId") String userId,
			@RequestBody String friendInfo) {
		List<FriendSheet> friendList = new ArrayList<>();
		if (friendInfo.contains("@student.hcmus.edu.vn")) {
			User user2 = repository.findByEmail(friendInfo);
			FriendSheet friendSheet = new FriendSheet(user2.getUser_id(), user2.getFullname(), user2.getProfile());
			friendList.add(friendSheet);
		} else {
			List<User> list1 = repository.findByFullname(friendInfo);
			for (User u : list1) {
				FriendSheet friendSheet = new FriendSheet(u.getUser_id(), u.getFullname(), u.getProfile());
				friendList.add(friendSheet);
			}
		}
		return friendList;
	}

	@PostMapping("/user/acceptFriend/{userId}/{friendId}/{nonId}")
	public @ResponseBody NonSheetCopy acceptFriend(@PathVariable("userId") String userId,
			@PathVariable("friendId") String friendId, @PathVariable("nonId") String nonId, @RequestBody String type,Authentication auth) {
		NonSheetCopy nonSheetCopy = null;
		User user = repository.findByEmail(auth.getName());
		CalculateTime calculateTime = new CalculateTime();
		if (type.equals("accept")) {
			Friends friend = friendRepository.findFriend(Integer.valueOf(userId), Integer.valueOf(friendId));
			Nontifications nontifications = nonRepository.findById(Integer.valueOf(nonId)).get();
			if (friend != null) {
				friend.setConfirmed(true);
				friend.setWaiting(false);
				friendRepository.save(friend);
				Nontifications newNon = CreateNontifications(user, Integer.valueOf(friendId), "acceptfriend");
				nonSheetCopy = new NonSheetCopy(newNon.getText(), calculateTime.calculateTime(newNon.getDate(), newNon.getTime()),user);
				nonRepository.delete(nontifications);
				return nonSheetCopy;
			} else
				return nonSheetCopy;
		} else
			return nonSheetCopy;
	}

	@PostMapping("/user/refuseFriend/{userId}/{friendId}/{nonId}")
	public @ResponseBody String refuseFriend(@PathVariable("userId") String userId,
			@PathVariable("friendId") String friendId, @PathVariable("nonId") String nonId, @RequestBody String type) {
		if (type.equals("accept")) {
			Friends friend = friendRepository.findFriend(Integer.valueOf(userId), Integer.valueOf(friendId));
			Nontifications nontifications = nonRepository.findById(Integer.valueOf(nonId)).get();
			if (friend != null) {
				friend.setConfirmed(true);
				friend.setWaiting(false);
				friendRepository.delete(friend);
				nonRepository.delete(nontifications);
				return "success";
			} else
				return "false";
		} else
			return "false";
	}

	@PostMapping("/user/seenNotify/{userId}")
	public @ResponseBody String seenNon(@PathVariable("userId") String userId) {
		List<Nontifications> nontifications = nonRepository.hasntSeen(Integer.valueOf(userId));
		for (Nontifications n : nontifications) {
			n.setHasSeen(true);
		}
		nonRepository.saveAll(nontifications);
		return "success";
	}

	public boolean isLiked(int userId, int postId) {
		Actions actions = new Actions();
		actions = actionRepository.likeOfPost(userId, postId, "like");
		if (actions == null)
			return false;
		return true;
	}

	public Nontifications CreateNontifications(User sender, Integer receiverId, String text) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		User receiver = repository.findById(receiverId).get();
		Nontifications nontifications = new Nontifications();
		//List<Nontifications> listNon = new ArrayList<>();
		nontifications.setReceiver(receiver);
		nontifications.setSender(sender);
		nontifications.setType(text);
		nontifications.setText(" đã đã chấp nhận lời mời kết bạn");
		nontifications.setDate(date);
		nontifications.setTime(time);
		//listNon.add(nontifications);
		//receiver.setNons2(listNon);
		nontifications = nonRepository.saveAndFlush(nontifications);
		return nontifications;
	}
}
