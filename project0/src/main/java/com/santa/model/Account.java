package com.santa.model;

import java.util.Objects;

public class Account {
	
	private int id;
	private String accountType;
	private int amount;
	private int clientId;

	public Account() {
		super();
	}
	
	public Account(int id, String accountType, int amount, int clientId) {
		this.id = id;
		this.accountType = accountType;
		this.amount = amount;
		this.clientId = clientId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, id, accountType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return amount == other.amount && id == other.id && Objects.equals(accountType, other.accountType);
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountType=" + accountType + ", amount=" + amount + "]";
	}
	
	
	
}