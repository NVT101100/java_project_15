package com.SocialNetwork.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.SocialNetwork.Entity.Friends;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Integer> {
	
	@Query(value="select f.friend_id,f.user1_id,f.user2_id,f.confirmed,f.blocked,f.waiting,f.date from friends f where (f.user1_id =?1 and f.user2_id=?2) or (f.user1_id=?2 and f.user2_id=?1)",nativeQuery=true)
	Friends findFriend(int userId,int friendId);
}
