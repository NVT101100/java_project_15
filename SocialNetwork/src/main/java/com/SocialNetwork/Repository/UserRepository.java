package com.SocialNetwork.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	 	
}

