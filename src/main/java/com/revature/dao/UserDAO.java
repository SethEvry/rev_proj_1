package com.revature.dao;

import java.util.List;

import com.revature.models.User;

public interface UserDAO {
	
	int createUser(User user);
	
	List<User> getUsers();
	
	User getUserByUsername(String username);
	
	boolean update(User user);

	
}
