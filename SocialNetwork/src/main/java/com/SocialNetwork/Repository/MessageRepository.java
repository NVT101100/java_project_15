package com.SocialNetwork.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.Entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
