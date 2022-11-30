package com.revature.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.models.Reimbursement;
import com.revature.util.JDBCH2ConnectionUtil;

class ReimbursementDAOImplTest {
	
	public static ReimbursementDAO dao;
	public static Reimbursement reimbursement;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//has 4 initial values, 2 status 1, 1 - 2, 1 - 3. 2 type 2, 1 - 3, 1 -4.
		dao = new ReimbursementDAOImpl(new JDBCH2ConnectionUtil());
		//same as first initial ticket
		reimbursement =  new Reimbursement(1, 670.33, null, null, "I became a man that day", 1,
				0, 1, 4);
		
		
		
	}
	@BeforeEach
	void pretiafyBeginning() {
		System.out.println("\n==========================================================================================");
	}
	
	@AfterEach
	void pretiafyEnding() {
		System.out.println("==========================================================================================\n");
	}

	@Test
	void testGetAllReimbursements() {
		List<Reimbursement> list = dao.getReimbursements();
		assertEquals(4,list.size());
	}
	
	@Test
	void testGetReimbursementsByUsername() {
		//all are by admin
		List<Reimbursement> list = dao.getReimbursements("admin");
		assertEquals(4,list.size());
	}
	
	@Test
	void testGetReimbursementsByStatus() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> statuses = new ArrayList<>();
		statuses.add("1");
		map.put("status", statuses);		
		List<Reimbursement> list = dao.getReimbursements(map);
		assertEquals(2, list.size());
		
	}
	@Test
	void testGetReimbursementsByType() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> types = new ArrayList<>();
		types.add("2");
		map.put("type", types);		
		List<Reimbursement> list = dao.getReimbursements(map);
		assertEquals(2, list.size());
		
	}
	
	@Test
	void testGetReimbursementsByBoth(){
		Map<String, List<String>> map = new HashMap<>();
		List<String> types = new ArrayList<>();
		List<String> statuses = new ArrayList<>();
		types.add("2");
		map.put("type", types);	
		statuses.add("1");
		map.put("status", statuses);
		List<Reimbursement> list = dao.getReimbursements(map);
		assertEquals(1, list.size());
	}
	
	@Test
	void testGetReimbursementById() {
		Reimbursement re = dao.getReimbursementById(1);
		assertEquals(reimbursement, re);
		
		
		
		
	}

	@Test
	void testUpdateReimbursement() {
		Reimbursement re = new Reimbursement(reimbursement);
		re.setAmount(50.55);
		re.setResolverId(1);
		assertTrue(dao.updateReimbursement(re));
		
	}
}
