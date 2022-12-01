package com.revature.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.service.ReimbursementService;
import com.revature.service.ReimbursementServiceImpl;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;
import com.revature.util.EncryptUtil;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class ReimbursementController {
	private static UserService uServ = new UserServiceImpl();
	private static ReimbursementService reServ = new ReimbursementServiceImpl();

	public static Logger logger = LoggerFactory.getLogger(ReimbursementController.class);

	public static Handler submitTicket = ctx -> {
		logger.info("ReimbursementController::submitTicket() - creating ticket... ");
		
		//create reimbursement object from res body
		String body = ctx.body();
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		Reimbursement re = om.readValue(body, Reimbursement.class);

		//get logged in user id
		String cookie = ctx.cookieStore().get("Auth-Token");
		String current = EncryptUtil.decrypt(cookie)[0];
		User currentUser = uServ.getUserByUsername(current);
		
		//set ticket author id to logged in user before sending to service layer
		re.setAuthorId(currentUser.getId());
		
		//if created succefully, returns true
		boolean isCreated = reServ.createTicket(re);
		if (isCreated) {
			ctx.html("Created a ticket!");
			ctx.status(HttpStatus.CREATED);
		} else {
			ctx.html("Error during creation. Try again.");
			ctx.status(HttpStatus.BAD_REQUEST);
		}

	};
	
	public static Handler processTicket = ctx -> {

		String cookie = ctx.cookieStore().get("Auth-Token");
		String currentUser = EncryptUtil.decrypt(cookie)[0];
		//check if user is a manager
		if (uServ.isManager(currentUser)) {
			//get ticket id from path
			int ticketId = Integer.parseInt(ctx.pathParam("id"));
			
			//get id of manager processing
			int userId = uServ.getUserByUsername(currentUser).getId();
			
			String body = ctx.body();
			
			//get status value from req body
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(body);
			int status = jsonNode.get("status").asInt();
			
			//update ticket, and if updated, gives 204 status
			boolean isUpdated = reServ.processTicket(ticketId, status, userId);
			if (isUpdated) {
				ctx.status(HttpStatus.NO_CONTENT);
			} else {
				ctx.status(HttpStatus.NOT_FOUND);
			}
		} else {
			ctx.status(HttpStatus.UNAUTHORIZED);
		}

	};

	// check if ?type=null or ?status=null
	public static Handler viewTickets = ctx -> {
		String cookie = ctx.cookieStore().get("Auth-Token");
		String currentUser = EncryptUtil.decrypt(cookie)[0];

		List<Reimbursement> targets;

		Map<String, List<String>> queries = ctx.queryParamMap();

		if (uServ.isManager(currentUser)) {
			if (!queries.isEmpty()) {
				//if there are queries
				targets = reServ.getTickets(queries);
			} else {
				//no queries
				targets = reServ.getTickets();
			}
		} else {
			//non-manager can only see their own tickets
			if (!queries.isEmpty()) {

				targets = reServ.getTickets(currentUser, queries);
			} else {

				targets = reServ.getTickets(currentUser);
			}
		}
		//only send list if not empty
		if (!targets.isEmpty()) {
			ctx.json(targets);
			ctx.status(HttpStatus.OK);
		} else {
			ctx.html("No tickets found");
			ctx.status(HttpStatus.NOT_FOUND);
		}

	};

	public static Handler viewTicket = ctx -> {
		
		//get user id
		String cookie = ctx.cookieStore().get("Auth-Token");
		String currentUser = EncryptUtil.decrypt(cookie)[0];
		User target = uServ.getUserByUsername(currentUser);
		
		//ticket id from path
		Reimbursement re = reServ.getTicket(Integer.parseInt(ctx.pathParam("id")));
		
		//make sure ticket exists
		if (re != null) {
			
			//user can only view ticket if its theirs or if they are a manager
			if (uServ.isManager(currentUser) || target.getId() == re.getAuthorId()) {
				ctx.json(re);
				ctx.status(HttpStatus.OK);
			} else {
				ctx.status(HttpStatus.UNAUTHORIZED);
			}
		} else {
			ctx.html("no ticket with that id");
			ctx.status(HttpStatus.NOT_FOUND);
		}
	};

}
