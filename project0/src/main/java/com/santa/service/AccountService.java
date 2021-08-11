package com.santa.service;

import java.sql.SQLException;
import java.util.List;

import com.santa.dao.AccountDAO;
import com.santa.dao.AccountDAOImpl;
import com.santa.dao.ClientDAO;
import com.santa.dao.ClientDAOImpl;
import com.santa.dto.AddOrEditAccountDTO;
import com.santa.exception.BadParameterException;
import com.santa.exception.ClientNotFoundException;
import com.santa.exception.DatabaseException;
import com.santa.model.Account;
import com.santa.model.Client;

public class AccountService {


	private AccountDAO accountDao;
	private ClientDAO clientDao;

	public AccountService() {
		this.accountDao = new AccountDAOImpl();
		this.clientDao = new ClientDAOImpl();
	}

	public AccountService(ClientDAO clientDao, AccountDAO accountDao) {
		this.clientDao = clientDao;
		this.accountDao = accountDao;
	}

	public List<Account> getAllAccountsFromClient(String clientIdString, String lEnd, String uEnd) throws ClientNotFoundException, DatabaseException, BadParameterException {
		try {
			int clientId = Integer.parseInt(clientIdString);
			int lowerEnd = -1;
			int upperEnd = -1;
			List<Account> accounts = null;

			try {
				if(lEnd != null) {
					lowerEnd = Integer.parseInt(lEnd);
				}
			} catch (NumberFormatException e) {
				throw new BadParameterException(uEnd + " was passed in by the user as the id, but it is not an int");
			}

			try {
				if (uEnd != null) {
					upperEnd = Integer.parseInt(uEnd);
				}
			} catch (NumberFormatException e) {
				throw new BadParameterException(uEnd + " was passed in by the user as the id, but it is not an int");
			}

			if(clientDao.getClientById(clientId) == null) {
				throw new ClientNotFoundException("Client with id " + clientId + " was not found");
			}

			if (lowerEnd != -1 && upperEnd != -1) {
				accounts = accountDao.getAllAccountsInRangeFromClient(clientId, lowerEnd, upperEnd);
			}
			else if (lowerEnd != -1) {
				accounts = accountDao.getGreaterThanAccounts(clientId, lowerEnd);
			}
			else if (upperEnd != -1) {
				accounts = accountDao.getLessThanAccounts(clientId, upperEnd);
			}
			else {
				accounts = accountDao.getAllAccountsFromClient(clientId);
			}

			return accounts;

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException(clientIdString + " was passed in by the user as the id, but it is not an int");
		}
	}

	public Account getSpecificAccountFromClient(String clientIdString, String accountIdString)  throws ClientNotFoundException, DatabaseException, BadParameterException {
		try {
			int clientId = Integer.parseInt(clientIdString);
			int accountId = Integer.parseInt(accountIdString);

			if(clientDao.getClientById(clientId) == null) {
				throw new ClientNotFoundException("Client with id " + clientId + " was not found");
			}
			if(accountDao.getSpecificAccountFromClient(clientId, accountId).getAccountType() == null) {
				throw new ClientNotFoundException("Account with id " + accountId + " was not found for client with id " + clientId);
			}

			Account account = accountDao.getSpecificAccountFromClient(clientId, accountId);

			return account;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException("One or both of the parameters passed in by the user as an id is not an int");
		}
	}

	public Account addAccountForClient(AddOrEditAccountDTO account, String clientIdString) throws DatabaseException, BadParameterException, ClientNotFoundException {
		try {
			int clientId = Integer.parseInt(clientIdString);

			if(clientDao.getClientById(clientId) == null) {
				throw new ClientNotFoundException("Client with id " + clientId + " was not found");
			}

			Account addedAccount = accountDao.addAccountForClient(account, clientId);
			return addedAccount;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	public Account updateSpecificAccountForClient(String stringClientId, String stringAccountId, AddOrEditAccountDTO account) throws DatabaseException, ClientNotFoundException, BadParameterException {

		try {
			int clientId = Integer.parseInt(stringClientId);
			int accountId = Integer.parseInt(stringAccountId);


			if (clientDao.getClientById(clientId) == null) {
				throw new ClientNotFoundException("Client with id " + clientId + " was not found");
			}

			Account editedAccount = accountDao.updateSpecificAccountForClient(clientId, accountId, account);

			return editedAccount;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException(stringClientId + " and " + stringAccountId + " wer passed in by the user as the ids, " + "but at least one is not an int");
		} 


	}

	public void deleteAccount(String stringClientId, String stringAccountId) throws BadParameterException, DatabaseException, ClientNotFoundException {

		try {
			int clientId = Integer.parseInt(stringClientId);
			int accountId = Integer.parseInt(stringAccountId);

			Client client = clientDao.getClientById(clientId);
			if (client == null) {
				throw new ClientNotFoundException("Trying to delete client with an id of " + clientId + ", but it does not exist");
			}

			Account account = accountDao.getSpecificAccountFromClient(clientId, accountId);
			if (account == null) {
				throw new ClientNotFoundException("Trying to delete account with an id of " + accountId + ", but it does not exist for this client");
			}

			accountDao.deleteAccount(clientId, accountId);			

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException(stringClientId + " was passed in by the user as the id, but it is not an int");
		}

	}



}
