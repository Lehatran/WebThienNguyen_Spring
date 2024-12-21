package com.example.webthiennguyen_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webthiennguyen_spring.entities.User;
import com.example.webthiennguyen_spring.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	public User getUser(String name) {
		return repo.getUser(name);
	}
	
}
