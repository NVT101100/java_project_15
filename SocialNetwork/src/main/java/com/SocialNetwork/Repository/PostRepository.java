package com.SocialNetwork.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.Posts;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
	
}
