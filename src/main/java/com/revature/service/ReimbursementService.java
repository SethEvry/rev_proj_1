package com.revature.service;

import java.util.List;
import java.util.Map;

import com.revature.models.Reimbursement;

public interface ReimbursementService {
	
	boolean createTicket(Reimbursement re);
	
	boolean processTicket(int ticketId, int statusId, int resolverId);
	
	
	List<Reimbursement> getTickets();
	List<Reimbursement> getTickets(Map<String, List<String>> args);
	List<Reimbursement> getTickets(String username);
	List<Reimbursement> getTickets(String username, Map<String, List<String>> args);
	
	Reimbursement getTicket(int id);

}
