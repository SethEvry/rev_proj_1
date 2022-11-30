package com.revature.controller;

import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class LoginController {
	
	private static UserService uServ = new UserServiceImpl();
	
	public static Handler showLogin = ctx -> {
		ctx.html("Please log in");
		ctx.status(HttpStatus.OK);
	};
	public static Handler handleLogin = ctx -> {
	
		boolean isLoggedIn = uServ.logIn(ctx.header("Authorization"));
		if(isLoggedIn) {
			String cookie = ctx.header("Authorization");
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
