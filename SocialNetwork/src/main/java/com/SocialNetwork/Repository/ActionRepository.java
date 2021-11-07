package com.SocialNetwork.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.Actions;
import com.SocialNetwork.Sheet.UserCommentSheet;

@Repository
public interface ActionRepository extends JpaRepository<Actions, Integer> {
	@Query(value="select l.action_id,l.user_id,l.post_id,l.type,l.date,l.time,l.text from actions l where l.user_id=?1 and l.post_id=?2 and l.type=?3",nativeQuery=true)
	Actions likeOfPost(int userId,int postId,String type);
	
	@Query(value="select a.action_id,a.post_id,a.user_id,a.type,a.date,a.time,a.text from actions a where a.post_id=?1 and a.type =?2",nativeQuery=true)
	List<Actions> findActionByType(Integer postId,String type);
}
