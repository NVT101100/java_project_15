package com.SocialNetwork.Controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.SocialNetwork.Entity.Actions;
import com.SocialNetwork.Entity.Nontifications;
import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.ActionRepository;
import com.SocialNetwork.Repository.NontificationRepository;
import com.SocialNetwork.Repository.PostRepository;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Service.CalculateTime;
import com.SocialNetwork.Sheet.CommentSheet;
import com.SocialNetwork.Sheet.LikeSheet;
import com.SocialNetwork.Sheet.NonSheetCopy;
import com.SocialNetwork.Sheet.PostSheet;
import com.SocialNetwork.Sheet.ReturnCommentSheet;
import com.SocialNetwork.Sheet.UserCommentSheet;

import aj.org.objectweb.asm.Type;
import ch.qos.logback.core.joran.action.Action;
import net.bytebuddy.asm.Advice.Return;

@Controller
public class PostController {

	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ActionRepository actionRepository;
	@Autowired
	NontificationRepository nonRepository;

	@PostMapping("/user/post/{userId}")
	public @ResponseBody Posts postStatus(@PathVariable("userId") String userId, @RequestBody PostSheet post) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		Posts posts = new Posts();
		List<Posts> listPost = new ArrayList<Posts>();
		User userpost = userRepository.findById(Integer.valueOf(userId)).get();
		posts.setImage(Base64.getEncoder().encodeToString(post.getImage()));
		posts.setStatus(post.getStatus());
		posts.setDate(date);
		posts.setTime(time);
		posts.setUserPost(userpost);
		listPost.add(posts);
		userpost.setPosts(listPost);
		postRepository.save(posts);
		userRepository.saveAndFlush(userpost);
		return posts;
	}

	@PostMapping("/user/action/like/{postId}")
	public @ResponseBody NonSheetCopy likePost(@PathVariable("postId") String postId, Authentication authentication,
			@RequestBody String type) {
		User user = userRepository.findByEmail(authentication.getName());
		CalculateTime calculateTime = new CalculateTime();
		Nontifications isSuccess = ActionLike(user.getUser_id(), Integer.valueOf(postId), type);
		if (isSuccess != null) {
			NonSheetCopy data = new NonSheetCopy(isSuccess.getText(),
					calculateTime.calculateTime(isSuccess.getDate(), isSuccess.getTime()), user);
			return data;
		} else
			return null;
	}

	@PostMapping("/user/deletePost/{postId}")
	public @ResponseBody String deletePost(@PathVariable("postId") String postId) {
		Posts posts = postRepository.findById(Integer.valueOf(postId)).get();
		if (posts != null) {
			actionRepository.deleteAll(posts.getActions());
			nonRepository.deleteAll(posts.getNontifications());
			postRepository.delete(posts);
			return "success";
		} else
			return "not found";
	}

	@PostMapping("/user/comment/{postId}")
	public @ResponseBody ReturnCommentSheet commentPost(@PathVariable("postId") String postId,
			@RequestBody String comment, Authentication authentication) {
		ReturnCommentSheet returnCommentSheet = ActionComment(userRepository.findByEmail(authentication.getName()), Integer.valueOf(postId), comment);
		return returnCommentSheet;
	}

	public Nontifications ActionLike(Integer userId, Integer postId, String type) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		User sender = userRepository.getById(userId);
		Nontifications non = null;
		Actions actions = actionRepository.likeOfPost(sender.getUser_id(), Integer.valueOf(postId), "like");
		if (actions == null && type.equals("like")) {
			Posts post = postRepository.findById(postId).get();
			User receiver = post.getUserPost();
			Actions action = new Actions();
			List<Actions> listLike = new ArrayList<Actions>();
			action.setType("like");
			action.setPost(post);
			action.setUser(sender);
			action.setDate(date);
			action.setTime(time);
			listLike.add(action);
			post.setAction(listLike);
			if (sender.getUser_id() != receiver.getUser_id()) {
				Nontifications nontifications = new Nontifications();
				List<Nontifications> listNon = new ArrayList<>();
				nontifications.setReceiver(receiver);
				nontifications.setSender(sender);
				nontifications.setType("likepost");
				nontifications.setText(" đã thích một bài viết của bạn");
				nontifications.setDate(date);
				nontifications.setTime(time);
				non = nontifications;
				listNon.add(nontifications);
				receiver.setNons2(listNon);
				post.setNontifications(listNon);
				nontifications.setPost(post);
				nonRepository.save(nontifications);
			}
			userRepository.saveAndFlush(sender);
			userRepository.saveAndFlush(receiver);
			postRepository.save(post);
			return non;
		} else if (actions != null && type.equals("unlike")) {
			actionRepository.delete(actions);
			return null;
		} else
			return non;
	}

	public ReturnCommentSheet ActionComment(User user, Integer postId, String commentText) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		CalculateTime calculateTime =  new CalculateTime();
		ReturnCommentSheet returnCommentSheet = null;
		Actions action = new Actions();
		Posts post = postRepository.findById(postId).get();
		User sender = user;
		User receiver = post.getUserPost();
		List<Actions> listComment = new ArrayList<Actions>();
		action.setType("comment");
		action.setText(commentText);
		action.setPost(post);
		action.setUser(sender);
		action.setDate(date);
		action.setTime(time);
		listComment.add(action);
		post.setAction(listComment);
		UserCommentSheet responeInfo = new UserCommentSheet(action.getUser().getFullname(),
				action.getUser().getProfile(), action.getAction_id(), action.getText());
		if (sender.getUser_id() != receiver.getUser_id()) {
			Nontifications nontifications = new Nontifications();
			List<Nontifications> listNon = new ArrayList<>();
			nontifications.setReceiver(receiver);
			nontifications.setSender(sender);
			nontifications.setType("likepost");
			nontifications.setText(" đã bình luận trong một bài viết của bạn");
			nontifications.setDate(date);
			nontifications.setTime(time);
			listNon.add(nontifications);
			receiver.setNons2(listNon);
			post.setNontifications(listNon);
			NonSheetCopy nonSheetCopy = new NonSheetCopy(nontifications.getText(),calculateTime.calculateTime(nontifications.getDate(), nontifications.getTime()) , sender);
			returnCommentSheet = new ReturnCommentSheet(responeInfo, nonSheetCopy );
		}
		else returnCommentSheet = new ReturnCommentSheet(responeInfo, null);
		userRepository.saveAndFlush(sender);
		userRepository.saveAndFlush(receiver);
		postRepository.saveAndFlush(post);
		return returnCommentSheet;
	}

}
