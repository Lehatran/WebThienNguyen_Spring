package com.example.webthiennguyen_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.webthiennguyen_spring.entities.User;
import com.example.webthiennguyen_spring.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService sv;
	
	@GetMapping("/")
	private String view(Model model) {
		return "views/HUONGNGU";
	}

	@GetMapping("/user/{name}")
	private String getUser(@PathVariable String name, Model model) {
		model.addAttribute("huongngu", sv.getUser(name));
		return "views/HUONGNGU";
	}

	@GetMapping("/user")
	private User getUserBody(@RequestBody User user) {
		return sv.getUser(null);
	}

}
