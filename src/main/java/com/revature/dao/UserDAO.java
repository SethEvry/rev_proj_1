package com.revature.dao;

import java.util.List;

import com.revature.models.User;

public interface UserDAO {
	
	public int createUser(User user);
	
	public List<User> getUsers();
	
	public User getUserByUsername(String username);
	
	public boolean update(User user);
	
}
