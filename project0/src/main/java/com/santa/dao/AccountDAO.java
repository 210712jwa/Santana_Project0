package com.santa.dao;

import java.sql.SQLException;
import java.util.List;

import com.santa.dto.AddOrEditAccountDTO;
import com.santa.model.Account;

public interface AccountDAO {

	public abstract List<Account> getAllAccountsFromClient(int clientId) throws SQLException;
	
	public abstract List<Account> getAllAccountsInRangeFromClient(int clientId, int lowerEnd, int upperEnd) throws SQLException;
	
	public abstract Account addAccountForClient(AddOrEditAccountDTO account, int clientId) throws SQLException;

	public abstract Account getSpecificAccountFromClient(int clientId, int accountId) throws SQLException;

	public abstract Account updateSpecificAccountForClient(int clientId, int accountId, AddOrEditAccountDTO account) throws SQLException;
	
	public abstract void deleteAccount(int clientId, int accountId) throws SQLException;

	public abstract List<Account> getGreaterThanAccounts(int clientId, int lowerEnd) throws SQLException;

	public abstract List<Account> getLessThanAccounts(int clientId, int upperEnd) throws SQLException;
}
