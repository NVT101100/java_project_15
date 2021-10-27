package com.SocialNetwork.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByEmail(String email);
}

