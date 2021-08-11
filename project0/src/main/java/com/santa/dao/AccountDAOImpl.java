package com.santa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.santa.dto.AddOrEditAccountDTO;
import com.santa.model.Account;
import com.santa.util.ConnectionUtility;

public class AccountDAOImpl implements AccountDAO {

	@Override
	public List<Account> getAllAccountsFromClient(int clientId) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			List<Account> accounts = new ArrayList<>();

			String sql = "SELECT * FROM project0.account a WHERE a.client_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("account_type");
				int amount = rs.getInt("amount");
				int clientIdFK = rs.getInt("client_id");

				Account a = new Account(id, accountType, amount, clientIdFK);
				accounts.add(a);
			}
			return accounts;
		}

	}

	@Override
	public List<Account> getAllAccountsInRangeFromClient(int clientId, int lowerEnd,  int upperEnd) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			List<Account> accounts = new ArrayList<>();

			String sql = "SELECT * FROM project0.account a WHERE a.client_id = ? AND amount BETWEEN ? AND ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);
			pstmt.setInt(2, lowerEnd);
			pstmt.setInt(3, upperEnd);
			

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("account_type");
				int amount = rs.getInt("amount");
				int clientIdFK = rs.getInt("client_id");

				Account a = new Account(id, accountType, amount, clientIdFK);
				accounts.add(a);
			}
			return accounts;
		}

	}
	
	@Override
	public List<Account> getGreaterThanAccounts(int clientId, int lowerEnd) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			List<Account> accounts = new ArrayList<>();

			String sql = "SELECT * FROM project0.account a WHERE a.client_id = ? AND amount >= ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);
			pstmt.setInt(2, lowerEnd);
			

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("account_type");
				int amount = rs.getInt("amount");
				int clientIdFK = rs.getInt("client_id");

				Account a = new Account(id, accountType, amount, clientIdFK);
				accounts.add(a);
			}
			return accounts;
		}

	}
	
	@Override
	public List<Account> getLessThanAccounts(int clientId, int upperEnd) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			List<Account> accounts = new ArrayList<>();

			String sql = "SELECT * FROM project0.account a WHERE a.client_id = ? AND amount <= ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);
			pstmt.setInt(2, upperEnd);
			

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String accountType = rs.getString("account_type");
				int amount = rs.getInt("amount");
				int clientIdFK = rs.getInt("client_id");

				Account a = new Account(id, accountType, amount, clientIdFK);
				accounts.add(a);
			}
			return accounts;
		}

	}

	@Override
	public Account getSpecificAccountFromClient(int clientId, int accountId) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			Account a = new Account();

			String sql = "SELECT * FROM project0.account a WHERE a.client_id = ? AND a.id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);
			pstmt.setInt(2, accountId);

			ResultSet rs = pstmt.executeQuery();

			if(rs.next()){
				int id = rs.getInt("id");
				String accountType = rs.getString("account_type");
				int amount = rs.getInt("amount");
				int clientIdFK = rs.getInt("client_id");

				a = new Account(id, accountType, amount, clientIdFK);
			}
			return a;

		}

	}
	
	@Override
	public Account updateSpecificAccountForClient(int clientId, int accountId, AddOrEditAccountDTO account) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {

			String sql = "UPDATE project0.account SET account_type = ?, amount = ? WHERE client_id = ? AND id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, account.getAccountType());
			pstmt.setInt(2, account.getAmount());
			pstmt.setInt(3, clientId);
			pstmt.setInt(4, accountId);

			int recordsUpdated = pstmt.executeUpdate();
			if (recordsUpdated != 1) {
				throw new SQLException("Record was not able to be updated");
			}
			
			return new Account(accountId, account.getAccountType(), account.getAmount(), clientId);

		}

	}

	@Override
	public Account addAccountForClient(AddOrEditAccountDTO account, int clientId) throws SQLException {
		try (Connection con = ConnectionUtility.getConnection()) {
			String sql = "INSERT INTO project0.account (account_type, amount, client_id) VALUES (?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, account.getAccountType());
			pstmt.setInt(2, account.getAmount());
			pstmt.setInt(3, clientId);

			int recordsUpdated = pstmt.executeUpdate(); 
			if (recordsUpdated != 1) {
				throw new SQLException("Could not insert an account record");
			}

			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				Account createdAccount = new Account(generatedKeys.getInt(1), account.getAccountType(), account.getAmount(), clientId);

				return createdAccount;
			} else {
				throw new SQLException("Autogenerated id could not be obtained for Account");
			}

		}
	}
	
	public void deleteAccount(int clientId, int accountId) throws SQLException {
		
		try (Connection con = ConnectionUtility.getConnection()) {
			String sql = "DELETE FROM project0.account WHERE id = ? AND client_id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, accountId);
			pstmt.setInt(2, clientId);
			
			int recordsDeleted = pstmt.executeUpdate();
			
			if (recordsDeleted != 1) {
				throw new SQLException("Record was not able to be deleted");
			}
		}
	}
}
