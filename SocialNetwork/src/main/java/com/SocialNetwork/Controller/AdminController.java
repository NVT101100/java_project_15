package com.SocialNetwork.Controller;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.SocialNetwork.Entity.Actions;
import com.SocialNetwork.Entity.GroupChat;
import com.SocialNetwork.Entity.Nontifications;
import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.ActionRepository;
import com.SocialNetwork.Repository.LoggedInCount;
import com.SocialNetwork.Repository.NontificationRepository;
import com.SocialNetwork.Repository.PostRepository;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Sheet.ChartSheet;
import com.SocialNetwork.Sheet.CountLogInSheet;
import com.SocialNetwork.Sheet.CountPostSheet;
import com.SocialNetwork.Sheet.CountUserSheet;
import com.SocialNetwork.Sheet.PostAdminSheet;
import com.SocialNetwork.Sheet.UserSheet;
@Controller
public class AdminController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private LoggedInCount loggedInCount;
	@Autowired
	ActionRepository actionRepository;
	@Autowired
	NontificationRepository nonRepository;
	@GetMapping("/admin/countManager")
	public ModelAndView countPage() {
		ModelAndView modelAndView = new ModelAndView("admins/countManager");
		List<User> users = userRepository.findAll();
		List<Posts> posts = postRepository.findAllSortByTime();
		List<com.SocialNetwork.Entity.LoggedInCount> loggedIn = loggedInCount.findAll();
		File file = new File("E:");
		Integer countUser = users.size();
		Integer countPost = posts.size();
		Integer countLogged = loggedIn.size();
		long totalDisk = file.getTotalSpace() / (1024 * 1024 * 1024);
		long usageDisk = totalDisk - file.getFreeSpace() / (1024 * 1024 * 1024);
		long totalRam = Runtime.getRuntime().maxMemory() / (1024 * 1024);
		long freeRam = Runtime.getRuntime().freeMemory() / (1024 * 1024);
		List<User> newUser;
		if (countUser > 5)
			newUser = users.subList(0, 5);
		else
			newUser = users;
		List<Posts> newPost;
		if (countPost > 5)
			newPost = posts.subList(0, 5);
		else
			newPost = posts;
		List<com.SocialNetwork.Entity.LoggedInCount> newLog;
		if (countLogged > 5)
			newLog = loggedIn.subList(0, 5);
		else
			newLog = loggedIn;
		modelAndView.addObject("countUser", countUser);
		modelAndView.addObject("countLoggedIn", countLogged);
		modelAndView.addObject("countPost", countPost);
		modelAndView.addObject("newUser", newUser);
		modelAndView.addObject("newPost", newPost);
		modelAndView.addObject("newLog", newLog);
		modelAndView.addObject("diskSpacePercent", usageDisk * 100 / totalDisk);
		modelAndView.addObject("diskFreeSpace", usageDisk);
		modelAndView.addObject("diskTotalSpace", totalDisk);
		modelAndView.addObject("ramUsed", freeRam);
		modelAndView.addObject("totalRam", totalRam);
		modelAndView.addObject("ramPercent", freeRam * 100 / totalRam);
		return modelAndView;
	}
	@PostMapping("/admin/getDataTable")
	public @ResponseBody ChartSheet GetData() {
		ChartSheet chartSheet;
		List<CountUserSheet> users = userRepository.countUserByDate();
		List<CountPostSheet> posts = postRepository.countPostByDate();
		List<CountLogInSheet> logs = loggedInCount.countLogByDate();
		chartSheet = new ChartSheet(users, posts, logs);
		return chartSheet;
	}
	@GetMapping("/admin/userManager")
	public ModelAndView userManagePage() {
		ModelAndView model = new ModelAndView("admins/userManager");
		List<User> users = userRepository.findAll();
		List<UserSheet> userSheets = new ArrayList<UserSheet>();
		for (User u : users) {
			int postNum = 0;
			int messNum = 0;
			postNum = u.getPosts().size();
			List<GroupChat> groupChats = new ArrayList<>();
			groupChats.addAll(u.getReceiveMessage());
			groupChats.addAll(u.getSendMessage());
			for (GroupChat g : groupChats) {
				messNum += g.getMessages().size();
			}
			UserSheet userSheet = new UserSheet(u, postNum, messNum);
			userSheets.add(userSheet);
		}
		model.addObject("users", userSheets);
		return model;
	}
	@PostMapping("/admin/activeAccount/{userId}")
	public @ResponseBody boolean activeAccount(@RequestBody String userStatus, @PathVariable("userId") String userId) {
		boolean dataReturn;
		if (userStatus.equals("enabled")) {
			userRepository.ActiveAndLock(0, Integer.valueOf(userId));
			dataReturn = false;
		} else {
			userRepository.ActiveAndLock(1, Integer.valueOf(userId));
			dataReturn = true;
		}
		return dataReturn;
	}
	@GetMapping("/admin/postManager")
	public ModelAndView postManagePage() {
		ModelAndView model = new ModelAndView("admins/postManager");
		List<Posts> posts = postRepository.findAll();
		List<PostAdminSheet> postAdminSheets = new ArrayList<>();
		for (Posts p : posts) {
			int numLike = 0;
			int numComment = 0;
			List<Actions> actions = p.getActions();
			for (Actions a : actions) {
				if (a.getType().equals("like"))
					numLike++;
				else if (a.getType().equals("comment"))
					numComment++;
			}PostAdminSheet postAdminSheet = new PostAdminSheet(p, numLike, numComment);
			postAdminSheets.add(postAdminSheet);
		}
		model.addObject("posts", postAdminSheets);
		return model;
	}
	@PostMapping("/admin/deletePost")
	public @ResponseBody int deletePost(@RequestBody int postId) {
		Posts posts = postRepository.getById(postId);
		if (posts != null) {
			List<Actions> actions = posts.getActions();
			List<Nontifications> nontifications = posts.getNontifications();
			nonRepository.deleteAll(nontifications);
			actionRepository.deleteAll(actions);
			postRepository.delete(posts);
			return postId;
		}
		else return -1;
	}
}

