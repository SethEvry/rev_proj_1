package com.revature.controller;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class UserController {

	private static UserService uServ = new UserServiceImpl();

	public static Logger logger = LoggerFactory.getLogger(UserController.class);

	public static Handler registerUser = ctx -> {
		logger.info("UserController::handleRegisterUser() - registering user...");

		String body = ctx.body();

		ObjectMapper om = new ObjectMapper();

		User target = om.readValue(body, User.class);

		logger.info("New User: " + target);

		boolean isCreated = uServ.registerUser(target);
		if (isCreated) {
			ctx.html("The new user has been created successfully.");
			ctx.status(HttpStatus.CREATED);

		} else {
			ctx.html("User creation failed.");
			ctx.status(HttpStatus.BAD_REQUEST);
		}
	};
	
	public static Handler handleViewUsers = ctx -> {
		String cookie = ctx.cookieStore().get("Auth-Token");
		byte[] bytes = Base64.getDecoder().decode(cookie);
		String currentUser = new String(bytes);
		if(uServ.isManager(currentUser)) {
			ctx.json(uServ.getUsers());
			ctx.status(HttpStatus.OK);
		} else {
			ctx.html("You need to be a manager to view all employees");
			ctx.status(HttpStatus.UNAUTHORIZED);
		}
		
	};
	public static Handler handleChangeRole = ctx -> {
		
		String cookie = ctx.cookieStore().get("Auth-Token");
		byte[] bytes = Base64.getDecoder().decode(cookie);
		String currentUser = new String(bytes); 
				
		if (uServ.isManager(currentUser)) {
			String body = ctx.body();
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(body);
			int role = jsonNode.get("role").asInt();
			boolean isChanged = uServ.changeRole(ctx.pathParam("username"), role);
			if (isChanged) {
				ctx.status(HttpStatus.NO_CONTENT);
			} else {
				ctx.status(HttpStatus.NOT_FOUND);
			}
		} else {
			ctx.html("You need to be a manager to change somebody's position");
			ctx.status(HttpStatus.UNAUTHORIZED);
		}

	};

}
