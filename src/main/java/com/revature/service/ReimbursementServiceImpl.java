package com.revature.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.ReimbursementDAO;
import com.revature.dao.ReimbursementDAOImpl;
import com.revature.models.Reimbursement;

public class ReimbursementServiceImpl implements ReimbursementService {

	private static ReimbursementDAO dao;
	public static Logger logger = LoggerFactory.getLogger(ReimbursementServiceImpl.class);

	public ReimbursementServiceImpl() {
		super();
		dao = new ReimbursementDAOImpl();
	}

	public ReimbursementServiceImpl(ReimbursementDAO reDao) {
		super();
		dao = reDao;

	}

	@Override
	public boolean createTicket(Reimbursement re) {
		return dao.createReimbursement(re) != 0;
	}

	@Override
	public boolean processTicket(int ticketId, int statusId, int resolverId) {
		Reimbursement ticket = dao.getReimbursementById(ticketId);

		if (ticket.getId() != 0) {

			if (ticket.getAuthorId() == resolverId) {
				logger.info("You can't change your own ticket!");
				return false;
			}
			if (ticket.getStatusId() != 1) {
				logger.info("Ticket status can't change after finished processing.");
				return false;
			}

			ticket.setStatusId(statusId);
			ticket.setResolverId(resolverId);
			return dao.updateReimbursement(ticket);

		}
		return false;
	}

	/**
	 * normal employees
	 */

	@Override
	public List<Reimbursement> getTickets(String username) {

		return dao.getReimbursements(username);
	}

	@Override
	public List<Reimbursement> getTickets(String username, Map<String, List<String>> args) {
		if (!args.isEmpty()) {
			return dao.getReimbursements(username, args);
		}
		return null;
	}

	/*
	 * Manager methods get called if isManager
	 * 
	 */
	@Override
	public List<Reimbursement> getTickets() {
		// TODO Auto-generated method stub
		return dao.getReimbursements();
	}

	@Override
	public List<Reimbursement> getTickets(Map<String, List<String>> args) {
		if (!args.isEmpty()) {
			return dao.getReimbursements(args);
		}
		return null;
	}

	/*
	 * view single ticket, if not author, can only view if manager
	 */
	public Reimbursement getTicket(int id) {
		Reimbursement re = dao.getReimbursementById(id);
		if (re.getId() != 0) {
			return re;
		}
		return null;
	}

}
