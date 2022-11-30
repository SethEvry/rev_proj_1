package com.revature.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.dao.ReimbursementDAO;
import com.revature.models.Reimbursement;



class ReimbursementServiceImplTest {
	
	@InjectMocks
	private static ReimbursementServiceImpl reServ;
	
	@Mock
	private static ReimbursementDAO dao;
	
	private static Reimbursement reimbursement =  new Reimbursement(1, 670.33, null, null, "I became a man that day", 1,
			0, 1, 4);
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@AfterEach
	void afterEach() {
		reServ = null;
		reset(dao);
	}

	@BeforeEach
	void pretiafyBeginning() {
		System.out.println(
				"\n==========================================================================================");
	}

	@AfterEach
	void pretiafyEnding() {
		System.out.println(
				"==========================================================================================\n");
	}

	@Test
	void testCreateTicket() {
		when(dao.createReimbursement(reimbursement)).thenReturn(1);
		assertEquals(true, reServ.createTicket(reimbursement));
		
	}
	
	

}
