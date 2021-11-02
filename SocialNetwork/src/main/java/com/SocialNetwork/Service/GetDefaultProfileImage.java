package com.SocialNetwork.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

public class GetDefaultProfileImage {
	private byte[] filebyte;
	private File file;
	
	public byte[] GetProfileImageBydefault(String gender) {
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
			filebyte = org.aspectj.util.FileUtil.readAsByteArray(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(filebyte);
		return filebyte;
	}
}
