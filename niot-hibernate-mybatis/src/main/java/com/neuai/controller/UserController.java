package com.neuai.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuai.bean.entity.Users;
import com.neuai.service.UserService;

/**
 * Created by neuai.com
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	@Autowired
	UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/save/{userId}/{userName}")
	public String save(@PathVariable String userId, @PathVariable String userName) {
		Users user = new Users();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setCreateTime(new Date());
		Users userRst = userService.save(user);
		return "success";
	}

	@GetMapping("/get/{userId}")
	public Users findOne(@PathVariable String userId) {
		Users user = userService.findById(userId);
		return user;
	}

	@GetMapping
	public List<Users> findAll() {
		return userService.findAll();
	}
}
