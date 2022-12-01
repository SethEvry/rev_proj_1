package com.revature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.LoginController;
import com.revature.controller.ReimbursementController;
import com.revature.controller.UserController;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class MainDriver {

	public static Logger logger = LoggerFactory.getLogger(MainDriver.class);

	public static void main(String[] args) {

		Javalin app = Javalin.create().start(9001);

		app.before((ctx) -> {
			logger.info("Request at URL " + ctx.url() + " has started.");
		});

		app.get("/hi", ctx -> {
			// ctx.html("Hello world!");
			ctx.status(HttpStatus.OK);
		});
		// =======================================================================
		/**
		 * handle login
		 */
		// ========================================================================
		// login
		app.get("/login", LoginController.showLogin);
		app.post("/login", LoginController.handleLogin);

		// logout
		app.delete("/logout", LoginController.handleLogout);

		// ===========================================================
		/**
		 * users
		 */
		// =======================================================================
		// get
		app.before("/users", LoginController.authenticate);
		app.get("/users", UserController.handleViewUsers);
		// create user
		app.post("/users/register", UserController.registerUser);
		// make sure logged in before being able to view/edit users
		app.before("/users/{username}/*", LoginController.authenticate);
		app.put("/users/{username}/role", UserController.handleChangeRole);

		// =======================================================================
		/**
		 * reimbursement
		 */
		// =======================================================================
		// have to be logged in to create/edit tickets
		app.before("/reimbursements", LoginController.authenticate);
		app.before("/reimbursements/*", LoginController.authenticate);
		// get
		app.get("/reimbursements", ReimbursementController.viewTickets);
		// put
		app.get("/reimbursements/{id}", ReimbursementController.viewTicket);
		app.put("/reimbursements/{id}", ReimbursementController.processTicket);
		// post
		app.post("/reimbursements/create", ReimbursementController.submitTicket);

	}

}
