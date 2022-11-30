package com.revature.service;

import java.util.Base64;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.models.User;

public class UserServiceImpl implements UserService {

	public static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static UserDAO dao;

	public UserServiceImpl() {
		dao = new UserDAOImpl();
	}

	public UserServiceImpl(UserDAO userDAO) {
		dao = userDAO;
	}

	@Override
	public boolean registerUser(User user) {
		logger.info("UserServiceImpl::registerUser() called. Creating new user...");
		if (dao.getUserByUsername(user.getUsername()).getUsername() != null) {
			logger.info("User already exists.");
			return false;
		} else {
			return dao.createUser(user) != 0 ? true : false;
		}

	}
	//takes basic auth header and separated it to array [username, password] then checks it against the database
	@Override
	public boolean logIn(String authHeader) {
		String encodedCreds = authHeader.substring(6).trim();
		String[] creds = new String(Base64.getDecoder().decode(encodedCreds)).split(":");
		User target = dao.getUserByUsername(creds[0]);
		if (target != null) {
			logger.info("Attempting to log in with username: " + creds[0]);
			boolean result = BCrypt.checkpw(creds[1], target.getPassword());
			logger.debug("And the result is: " + result); 
			return result;

		}
		return false;
	}

	@Override
	public boolean isManager(String username) {
		if (username != null) {
			logger.info("UserServiceImpl::isManager() - Checking if " + username + " is a manager");
			if (dao.getUserByUsername(username).getRoleId() == 2) {
				logger.info(username + " is a Manager.");
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<User> getUsers(){
		logger.info("Listing all users");
		return dao.getUsers();
	}

	@Override
	public User getUserByUsername(String username) {
		logger.info("Finding user: "+username);
		return dao.getUserByUsername(username);

	}
	
	@Override
	public boolean changeRole(String username, int roleId) {
		logger.info("Attempting to update user: "+ username + " to role: "+ roleId);
		User user = dao.getUserByUsername(username);
		user.setRoleId(roleId);

		return dao.update(user);
	}
}
