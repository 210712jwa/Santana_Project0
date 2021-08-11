package com.santa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.santa.dao.AccountDAO;
import com.santa.dao.ClientDAO;
import com.santa.dto.AddOrEditAccountDTO;
import com.santa.exception.BadParameterException;
import com.santa.exception.DatabaseException;
import com.santa.exception.ClientNotFoundException;
import com.santa.model.Account;
import com.santa.model.Client;

public class AccountServiceTest {

	private AccountService accountService;
	private ClientDAO clientDao;
	private AccountDAO accountDao;
	
	@Before
	public void setUp() {
		this.clientDao = mock(ClientDAO.class);
		this.accountDao = mock(AccountDAO.class);
		
		this.accountService = new AccountService(clientDao, accountDao);
	}
	

	@Test
	public void test_getAccountFromClient_positive() throws BadParameterException, DatabaseException, ClientNotFoundException, SQLException {
		
		when(clientDao.getClientById(eq(10))).thenReturn(new Client(10, "Bob", 100));
		
		List<Account> mockAccounts = new ArrayList<>();
		mockAccounts.add(new Account(1, "savings", 30, 10));
		mockAccounts.add(new Account(2, "checking", 33, 10));
		
		when(accountDao.getAllAccountsFromClient(eq(10))).thenReturn(mockAccounts);
		
		List<Account> actualAccounts = accountService.getAllAccountsFromClient("10", null, null);
		
		assertEquals(mockAccounts, actualAccounts);
	}
	
	@Test
	public void test_getAccountFromClient_clientDoesNotExist() throws BadParameterException, DatabaseException, ClientNotFoundException, SQLException {
		
		try {
			when(clientDao.getClientById(eq(10))).thenReturn(null);
			
			accountService.getAllAccountsFromClient("10", null, null);
			
			fail();
		} catch(ClientNotFoundException e) {
			assertEquals("Client with id 10 was not found", e.getMessage());
		}
		
	}
	
	@Test
	public void test_getAccountFromClient_invalidFormatClientId() throws DatabaseException, ClientNotFoundException {
		try {
			accountService.getAllAccountsFromClient("abc", null, null);
			
			fail();
		} catch(BadParameterException e) {
			assertEquals("abc was passed in by the user as the id, but it is not an int", e.getMessage());
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_getAccountFromClient_SQLExceptionEncountered_fromClientDao_getClientById() throws SQLException, BadParameterException, DatabaseException, ClientNotFoundException {
		when(clientDao.getClientById(eq(10))).thenThrow(SQLException.class);
		
		accountService.getAllAccountsFromClient("10", null, null);
	}
	
	@Test(expected = DatabaseException.class)
	public void test_getAccountFromClient_SQLExceptionEncountered_fromAccountDao_getAllAccountsFromClient() throws SQLException, BadParameterException, DatabaseException, ClientNotFoundException {
		when(clientDao.getClientById(eq(10))).thenReturn(new Client(10, "Bob", 25));
		
		when(accountDao.getAllAccountsFromClient(eq(10))).thenThrow(SQLException.class);
		
		accountService.getAllAccountsFromClient("10", null, null);
	}
	
	@Test
	public void test_getSpecificAccountFromClient_existingId() throws SQLException, DatabaseException, ClientNotFoundException, BadParameterException {
		when(accountDao.getSpecificAccountFromClient(eq(1),eq(1))).thenReturn(new Account(1, "checking", 100, 1));
		when(clientDao.getClientById(eq(1))).thenReturn(new Client (1, "Bob", 32));
		
		Account actual = accountService.getSpecificAccountFromClient("1", "1");
		
		Account expected = new Account(1, "checking", 100, 1);
		
		assertEquals(expected, actual);
	}

	@Test
	public void test_updateSpecificAccountForClient_positivePath() throws DatabaseException, ClientNotFoundException, BadParameterException, SQLException {
		AddOrEditAccountDTO dto = new AddOrEditAccountDTO();
		dto.setAccountType("checking");
		dto.setAmount(100);
		dto.setClientId(1);
		
		Account accountWithId10 = new Account(10, "checking", 100, 1);
		Client clientWithId10 = new Client (1, "bill", 32);
		when(accountDao.getSpecificAccountFromClient(eq(10), eq(1))).thenReturn(accountWithId10);
		when(clientDao.getClientById(eq(10))).thenReturn(clientWithId10);
		
		when(accountDao.updateSpecificAccountForClient(eq(10),eq(1), eq(dto))).thenReturn(new Account(10, "checking", 100, 1));
		
		Account actual = accountService.updateSpecificAccountForClient("10", "1", dto);
		
		Account expected = new Account(10, "checking", 100, 1);

		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_updateSpecificAccountForClient_clientDoesNotExist() throws DatabaseException, BadParameterException {
		AddOrEditAccountDTO dto = new AddOrEditAccountDTO();
		dto.setAccountType("Black Pearl");
		dto.setAmount(100);
		dto.setClientId(1);
		
		try {
			accountService.updateSpecificAccountForClient("1000", "1", dto);
			
			fail();
		} catch (ClientNotFoundException e) {
			assertEquals("Client with id 1000 was not found", e.getMessage());
		}
		
	}
	
	@Test(expected = BadParameterException.class)
	public void test_updateSpecificAccountForClient_invalidId() throws DatabaseException, ClientNotFoundException, BadParameterException {
		AddOrEditAccountDTO dto = new AddOrEditAccountDTO();
		dto.setAccountType("checking");
		dto.setAmount(100);
		dto.setClientId(1);
		
		accountService.updateSpecificAccountForClient("abc", "1", dto);
	}
	
	@Test(expected = DatabaseException.class)
	public void test_updateSpecificAccountForClient_SQLExceptionEncountered() throws SQLException, DatabaseException, ClientNotFoundException, BadParameterException {
		AddOrEditAccountDTO dto = new AddOrEditAccountDTO();
		dto.setAccountType("checking");
		dto.setAmount(100);
		dto.setClientId(1);
		
		when(accountDao.getSpecificAccountFromClient(eq(3), eq(10))).thenReturn(new Account(10, "saving", 500, 3));
		when(clientDao.getClientById(eq(3))).thenReturn(new Client (3, "bill", 32));
		when(accountDao.updateSpecificAccountForClient(eq(3), eq(10), eq(dto))).thenThrow(SQLException.class);
		
		accountService.updateSpecificAccountForClient("3", "10", dto);
	}
	
	@Test(expected = BadParameterException.class)
	public void test_deleteAccount_invalidId() throws DatabaseException, ClientNotFoundException, BadParameterException {
		
		
		accountService.deleteAccount("abc", "abc");
	}
}