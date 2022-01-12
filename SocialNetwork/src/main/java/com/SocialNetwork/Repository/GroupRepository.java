package com.SocialNetwork.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.GroupChat;

@Repository
public interface GroupRepository extends JpaRepository<GroupChat, Integer>{
	@Query(value="select * from groupchat as g where (g.receiver_id=?1 and g.sender_id=?2) or (g.sender_id=?1 and g.receiver_id=?2)",nativeQuery = true)
	List<GroupChat> findHasChatted(int userId,int friendId);
}
