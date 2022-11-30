package com.revature.dao;

import java.util.List;
import java.util.Map;

import com.revature.models.Reimbursement;

public interface ReimbursementDAO {
	
	public int createReimbursement(Reimbursement reimb);
		
		
	//managers see all
	public List<Reimbursement> getReimbursements();
	public List<Reimbursement> getReimbursements(Map<String, List<String>> arg);
	public boolean updateReimbursement(Reimbursement reimb);
	
	//employees see their own
	public List<Reimbursement> getReimbursements(String username);
	public List<Reimbursement> getReimbursements(String username, Map<String, List<String>> args);
	
	//get by id
	public Reimbursement getReimbursementById(int id);

}
