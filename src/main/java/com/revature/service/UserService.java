package com.revature.service;

import java.util.List;

import com.revature.models.User;

public interface UserService {
	
	public boolean registerUser(User user);
	
	public boolean changeRole(String username, int roleId);
	
	public List<User> getUsers();
	
	public User getUserByUsername(String username);
	
	public boolean logIn(String username, String password);
	
	public boolean isManager(String username);
	

}
