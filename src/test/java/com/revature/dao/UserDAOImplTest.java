package com.revature.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.models.User;
import com.revature.util.JDBCH2ConnectionUtil;

class UserDAOImplTest {
	
	public static UserDAO dao; 
	public static User user;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//h2 in-memory db, data clears after connection close.
		dao = new UserDAOImpl(new JDBCH2ConnectionUtil());
		user = new User("potato", "potato123", "Po", "Tato", "potato@hotmail.com");
		
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
	void testCreateUser() {
		//returns id of new user
		int result = dao.createUser(user);
		assertEquals(2, result);
		
	}
	
	@Test
	void TestGetUserByUsername() {
		//user created when h2 db created
		String username = "admin";
		assertEquals(username, dao.getUserByUsername(username).getUsername());
	}
	
	@Test
	void testUpdate() {
		User admin = dao.getUserByUsername("admin");
		admin.setEmail("notadmin@admin.com");
		// returns true if update succeeded
		assertTrue(dao.update(admin));
	}
	

}
