package com.SocialNetwork.Entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="groupchat",schema="socialnetwork")
public class GroupChat {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer group_id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sender_id",referencedColumnName="user_id")
	@JsonBackReference
	public User sender;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="receiver_id",referencedColumnName="user_id")
	@JsonBackReference
	public User receiver;
	
	@OneToMany(mappedBy="group",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<Message> messages;
	
	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
}
