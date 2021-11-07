package com.SocialNetwork.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import org.springframework.util.ResourceUtils;

public class GetDefaultProfileImage {
	private String filebyte;
	private File file;
	
	public String GetProfileImageBydefault(String gender) {
		if (gender.equals("male")) {
				try {
					file = ResourceUtils.getFile("classpath:defaultImage/default-avatar-boy.jpg");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else {
			try {
				file = ResourceUtils.getFile("classpath:defaultImage/default-avatar-girl.jpg");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			filebyte = Base64.getEncoder().encodeToString(org.aspectj.util.FileUtil.readAsByteArray(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filebyte;
	}
	
	public String setDefaultCover() {
		try {
			file = ResourceUtils.getFile("classpath:defaultImage/default-cover.png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			filebyte = Base64.getEncoder().encodeToString(org.aspectj.util.FileUtil.readAsByteArray(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filebyte;
	}
}
