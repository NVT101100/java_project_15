package com.SocialNetwork.Sheet;

import com.SocialNetwork.Entity.Nontifications;
import com.SocialNetwork.Entity.User;

public class NonSheetCopy {
	public String text;
	public String time;
	public User sender;
	public Nontifications non;
	public NonSheetCopy(String nontifications, String time,User sender) {
		this.text = nontifications;
		this.time = time;
		this.sender = sender;
	}
	public NonSheetCopy(Nontifications nontifications, String time,User sender) {
		this.non = nontifications;
		this.time = time;
		this.sender = sender;
	}
}
