package com.SocialNetwork.Sheet;

import java.lang.reflect.Array;
import java.util.List;

public class ChartSheet {
	
	public List<CountUserSheet> userData;
	public List<CountPostSheet> postData;
	public List<CountLogInSheet> logData;
	public ChartSheet(List<CountUserSheet> users, List<CountPostSheet> posts, List<CountLogInSheet> logs) {
		this.userData = users;
		this.postData = posts;
		this.logData = logs;
	}
}
