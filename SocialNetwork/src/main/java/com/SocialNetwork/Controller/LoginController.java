package com.SocialNetwork.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public ModelAndView loginPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("users/login");
		model.addObject("isSuccess", "none");
		return model;
	}
	
	@CrossOrigin(origins = "http://localhost:8080/SocialNetwork")
	@GetMapping("/login/error")
	public ModelAndView loginFailed() {
		ModelAndView model = new ModelAndView();
		model.setViewName("users/login");
		model.addObject("warningmessage", "Tài khoản hoặc mật khẩu không chính xác!");
		model.addObject("isSuccess", "none");
		return model;
	}
	
	
}
