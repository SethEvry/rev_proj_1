package com.revature.controller;

import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.LoginTemplate;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class LoginController {
	
	private static UserService uServ = new UserServiceImpl();
	
	public static Handler showLogin = ctx -> {
		ctx.html("Please log in: {username: username, password: password}");
		ctx.status(HttpStatus.OK);
	};
	public static Handler handleLogin = ctx -> {
		ObjectMapper om = new ObjectMapper();
		LoginTemplate temp = om.readValue(ctx.body(), LoginTemplate.class);
		
		boolean isLoggedIn = uServ.logIn(temp.getUsername(), temp.getPassword());
		if(isLoggedIn) {
			String cookie = Base64.getEncoder().encodeToString(temp.getUsername().getBytes());
			ctx.cookieStore().set("Auth-Token", cookie);		
			ctx.html("Logged in Succesfully");
			ctx.status(HttpStatus.OK);
		} else {
			ctx.html("Log in failed");
			ctx.status(HttpStatus.FORBIDDEN);
		}
		
	};
	public static Handler handleLogout = ctx -> {
		ctx.cookieStore().clear();
		ctx.html("Logged out");
		ctx.status(HttpStatus.OK);
	};
	public static Handler authenticate = ctx -> {
		if(ctx.cookieStore().get("Auth-Token") == null) {
			ctx.redirect("/login");
		} 
	};
}
