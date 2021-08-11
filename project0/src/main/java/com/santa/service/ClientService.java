package com.santa.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.santa.dao.AccountDAO;
import com.santa.dao.AccountDAOImpl;
import com.santa.dao.ClientDAO;
import com.santa.dao.ClientDAOImpl;
import com.santa.dto.AddOrEditClientDTO;
import com.santa.exception.BadParameterException;
import com.santa.exception.ClientNotFoundException;
import com.santa.exception.DatabaseException;
import com.santa.model.Account;
import com.santa.model.Client;

public class ClientService {

	private Logger logger = LoggerFactory.getLogger(ClientService.class);

	private ClientDAO clientDao;
	private AccountDAO accountDao;

	public ClientService() {
		this.clientDao = new ClientDAOImpl();
		this.accountDao = new AccountDAOImpl();
	}

	public ClientService(ClientDAO mockedClientDaoObject, AccountDAO mockedAccountDaoObject) {
		this.clientDao = mockedClientDaoObject;
		this.accountDao = mockedAccountDaoObject;
	}

	public List<Client> getAllClients() throws DatabaseException {
		List <Client> clients;
		try {
			clients = clientDao.getAllClients();

			for (Client client : clients) {
				List<Account> accounts = accountDao.getAllAccountsFromClient(client.getId());
				client.setAccounts(accounts);
			}

		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}

		return clients;
	}

	public Client getClientById(String stringId) throws DatabaseException, ClientNotFoundException, BadParameterException {
		try {
			int id = Integer.parseInt(stringId);

			Client client = clientDao.getClientById(id);

			if (client == null) {
				throw new ClientNotFoundException("Client with id " + id + " was not found");
			}

			List<Account> accounts = accountDao.getAllAccountsFromClient(id);
			client.setAccounts(accounts);

			return client;
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} catch (NumberFormatException e) {
			throw new BadParameterException(stringId + " was passed in by the user as the id, but it is not an int");
		}
	}

	public Client addClient(AddOrEditClientDTO client) throws DatabaseException, BadParameterException {
		if (client.getName().trim().equals("") && client.getAge() < 0) {
			throw new BadParameterException("Client name cannot be blank and age cannot be less than 0");
		}

		if (client.getName().trim().equals("")) {
			throw new BadParameterException("Client name cannot be blank");
		}

		if (client.getAge() < 0) {
			throw new BadParameterException("Client age cannot be less than 0");
		}

		try {
			Client addedClient = clientDao.addClient(client);
			addedClient.setAccounts(new ArrayList<>());

			return addedClient;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	public Client editClient(String stringId, AddOrEditClientDTO client) throws DatabaseException, ClientNotFoundException, BadParameterException {

		try {
			int clientId = Integer.parseInt(stringId);

			if (clientDao.getClientById(clientId) == null) {
				throw new ClientNotFoundException("Client with id " + clientId + " was not found");
			}

			Client editedClient = clientDao.editClient(clientId, client);

			List<Account> accounts = accountDao.getAllAccountsFromClient(clientId);
			editedClient.setAccounts(accounts);

			return editedClient;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException(stringId + " was passed in by the user as the id, " + "but it is not an int");
		}

	}

	public void deleteClient(String clientId) throws BadParameterException, DatabaseException, ClientNotFoundException {

		try {
			int id = Integer.parseInt(clientId);

			Client client = clientDao.getClientById(id);
			if (client == null) {
				throw new ClientNotFoundException("Trying to delete client with an id of " + id + ", but it does not exist");
			}

			clientDao.deleteClient(id);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException(clientId + " was passed in by the user as the id, " + "but it is not an int");
		}

	}



}
