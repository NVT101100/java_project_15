package com.SocialNetwork.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.GroupChat;

@Repository
public interface GroupRepository extends JpaRepository<GroupChat, Integer>{
	
}
