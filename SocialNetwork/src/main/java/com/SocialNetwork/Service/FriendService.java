package com.SocialNetwork.Service;

import java.util.ArrayList;
import java.util.List;

import com.SocialNetwork.Entity.Friends;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Sheet.FriendSheet;

public class FriendService {
	public static List<FriendSheet> listFriend(User user) {
		List<FriendSheet> listFriend = new ArrayList<FriendSheet>();
		FriendSheet sheet;
		List<Friends> listOnDB1 = user.getFriends1();
		List<Friends> listOnDB2 = user.getFriends2();
		for (Friends f : listOnDB1) {
			if (!f.isBlocked() && !f.isWaiting() && f.isConfirmed()) {
				sheet = new FriendSheet(f.getUser1().getUser_id(), f.getUser1().getFullname(),
						f.getUser1().getProfile());
				listFriend.add(sheet);
			}
		}
		for (Friends f : listOnDB2) {
			if (!f.isBlocked() && !f.isWaiting() && f.isConfirmed()) {
				sheet = new FriendSheet(f.getUser2().getUser_id(), f.getUser2().getFullname(),
						f.getUser2().getProfile());
				listFriend.add(sheet);
			}
		}
		return listFriend;
	}
}
