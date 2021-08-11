package com.santa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.santa.dao.AccountDAO;
import com.santa.dao.ClientDAO;
import com.santa.dto.AddOrEditClientDTO;
import com.santa.exception.BadParameterException;
import com.santa.exception.DatabaseException;
import com.santa.exception.ClientNotFoundException;
import com.santa.model.Client;

public class ClientServiceTest {
	
	private ClientService clientService;
	private ClientDAO clientDao;
	private AccountDAO accountDao;
	
	@Before
	public void setUp() throws Exception {
		this.clientDao = mock(ClientDAO.class); 
		this.accountDao = mock(AccountDAO.class);
		
		this.clientService = new ClientService(clientDao, accountDao); 
		}
	
	@Test
	public void test_getAllClients_negative() throws SQLException {
		when(clientDao.getAllClients()).thenThrow(SQLException.class);
		
		try {
			clientService.getAllClients();
			
			fail(); 
			} catch (DatabaseException e) {
			String exceptionMessage = e.getMessage();
			assertEquals("Something went wrong with our DAO operations", exceptionMessage);
		}
		
	}
	
	@Test
	public void test_getClientById_idStringIsNotAnInt() throws DatabaseException, ClientNotFoundException {
		try {
			clientService.getClientById("abc");
			
			fail();
		} catch (BadParameterException e) {
			assertEquals("abc was passed in by the user as the id, but it is not an int", e.getMessage());
		}
	}
	
	@Test
	public void test_getClientById_existingId() throws SQLException, DatabaseException, ClientNotFoundException, BadParameterException {
		when(clientDao.getClientById(eq(1))).thenReturn(new Client(1, "Bobby Bob", 1));
		
		Client actual = clientService.getClientById("1");
		
		Client expected = new Client(1, "Bobby Bob", 1);
		expected.setAccounts(new ArrayList<>());
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_getClientById_clientDoesNotExist() throws DatabaseException, ClientNotFoundException, BadParameterException {
		try {
			clientService.getClientById("10"); 
			
			fail();
		} catch (ClientNotFoundException e) {
			assertEquals("Client with id 10 was not found", e.getMessage());
		}
	}
	
	@Test
	public void test_getClientById_sqlExceptionEncountered() throws SQLException, ClientNotFoundException, BadParameterException {
		try {
			when(clientDao.getClientById(anyInt())).thenThrow(SQLException.class);
			
			clientService.getClientById("1");
			
			fail();
		} catch (DatabaseException e) {
			assertEquals("Something went wrong with our DAO operations", e.getMessage());
		}
	}
	

	@Test
	public void test_addClient_positivePath() throws SQLException, DatabaseException, BadParameterException {
		
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(10);
		
		when(clientDao.addClient(eq(dto))).thenReturn(new Client(1, "Bobby Bob", 10));
		
		Client actual = clientService.addClient(dto);
		
		Client expected = new Client(1, "Bobby Bob", 10);
		expected.setAccounts(new ArrayList<>());
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_addClient_blankName() throws DatabaseException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("");
		dto.setAge(18);
		
		try {
			clientService.addClient(dto);
			
			fail();
		} catch (BadParameterException e) {
			assertEquals("Client name cannot be blank", e.getMessage());
		}
		
	}
	
	@Test
	public void test_addClient_blankNameWithSpaces() throws DatabaseException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("                        ");
		dto.setAge(18);
		
		try {
			clientService.addClient(dto);
			
			fail();
		} catch (BadParameterException e) {
			assertEquals("Client name cannot be blank", e.getMessage());
		}
	}

	@Test
	public void test_addClient_negativeAge() throws DatabaseException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(-1);
		
		try {
			clientService.addClient(dto);
			
			fail();
		} catch (BadParameterException e) {
			assertEquals("Client age cannot be less than 0", e.getMessage());
		}
		
	}
	
	@Test
	public void test_addClient_negativeAgeAndBlankName() throws DatabaseException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("");
		dto.setAge(-10);
		
		try {
			clientService.addClient(dto);
			
			fail();
		} catch (BadParameterException e) {
			assertEquals("Client name cannot be blank and age cannot be less than 0", e.getMessage());
		}
		
	}
	
	@Test(expected = DatabaseException.class)
	public void test_addClient_SQLExceptionEncountered() throws SQLException, DatabaseException, BadParameterException {
		when(clientDao.addClient(any())).thenThrow(SQLException.class);
		
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(18);
		clientService.addClient(dto);
	}
	
	@Test
	public void test_editClient_positivePath() throws DatabaseException, ClientNotFoundException, BadParameterException, SQLException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(100);
		
		Client clientWithId10 = new Client(10, "Kyle Cat", 5);
		when(clientDao.getClientById(eq(10))).thenReturn(clientWithId10);
		
		when(clientDao.editClient(eq(10), eq(dto))).thenReturn(new Client(10, "Bobby Bob", 100));
		
		Client actual = clientService.editClient("10", dto);
		
		Client expected = new Client(10, "Bobby Bob", 100);
		expected.setAccounts(new ArrayList<>());
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_editClient_clientDoesNotExist() throws DatabaseException, BadParameterException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(100);
		
		try {
			clientService.editClient("1000", dto);
			
			fail();
		} catch (ClientNotFoundException e) {
			assertEquals("Client with id 1000 was not found", e.getMessage());
		}
		
	}
	
	@Test(expected = BadParameterException.class)
	public void test_editClient_invalidId() throws DatabaseException, ClientNotFoundException, BadParameterException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(100);
		
		clientService.editClient("abc", dto);
	}
	
	@Test(expected = DatabaseException.class)
	public void test_editClient_SQLExceptionEncountered() throws SQLException, DatabaseException, ClientNotFoundException, BadParameterException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setName("Bobby Bob");
		dto.setAge(100);
		
		when(clientDao.getClientById(eq(10))).thenReturn(new Client(10, "Kyle Cat", 5));
		when(clientDao.editClient(eq(10), eq(dto))).thenThrow(SQLException.class);
		
		clientService.editClient("10", dto);
	}
	
	@Test
	public void test_deleteClient_clientDoesNotExist() throws DatabaseException, BadParameterException {
		
		try {
			clientService.deleteClient("1000");
			
			fail();
		} catch (ClientNotFoundException e) {
			assertEquals("Trying to delete client with an id of 1000, but it does not exist", e.getMessage());
		}
		
	}
	
	@Test(expected = BadParameterException.class)
	public void test_deleteClient_invalidId() throws DatabaseException, ClientNotFoundException, BadParameterException {
		
		
		clientService.deleteClient("abc");
	}
}