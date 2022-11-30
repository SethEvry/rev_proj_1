package com.revature.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.dao.UserDAO;
import com.revature.models.User;

class UserServiceImplTest {

	@InjectMocks
	private static UserServiceImpl uServ;

	@Mock
	private static UserDAO dao;

	private static User user = new User(1, "potato", "potaat123", "Po", "Tato", "po.tato@netscape.net", 1);

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void afterEach() {
		uServ = null;
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
	void testRegisterUser() {
		when(dao.createUser(user)).thenReturn(1);
		boolean result = uServ.registerUser(user);
		assertTrue(result);

		verify(dao).createUser(user);
	}

	@Test
	void testLogin() {
		when(dao.getUserByUsername(user.getUsername())).thenReturn(user);

		assertTrue(uServ.logIn(user.getUsername(), user.getPassword()));

	}
	
	@Test
	void testChangeRole() {
		when(dao.getUserByUsername(user.getUsername())).thenReturn(user);
		when(dao.update(user)).thenReturn(true);
		
		assertTrue(uServ.changeRole(user.getUsername(), 2));
		
	}
	
	@Test
	void testGetUserByUsername() {
		when(dao.getUserByUsername(user.getUsername())).thenReturn(user);
		assertEquals(user, uServ.getUserByUsername(user.getUsername()));
	}
	
	@Test
	void testIsManager() {
		when(dao.getUserByUsername(user.getUsername())).thenReturn(user);
		assertFalse(uServ.isManager(user.getUsername()));
	}

}
