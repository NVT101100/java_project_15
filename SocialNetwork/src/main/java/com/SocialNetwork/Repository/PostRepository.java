package com.SocialNetwork.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.Posts;
import com.SocialNetwork.Sheet.CountPostSheet;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
	
	@Query(value="select p.post_id,p.date,p.time,p.image,p.status,p.user_id from posts p order by p.date desc,p.time desc",nativeQuery = true)
	List<Posts> findAllSortByTime();
	
	@Query(name="Posts.countPostByDate",nativeQuery=true)
	List<CountPostSheet> countPostByDate();
}
