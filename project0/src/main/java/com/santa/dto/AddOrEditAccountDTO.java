package com.santa.dto;

import java.util.Objects;

public class AddOrEditAccountDTO {
	
	private String account_type;
	private int amount;
	private int clientId;
	
	public AddOrEditAccountDTO() {
		super();
	}

	public String getAccountType() {
		return account_type;
	}

	public void setAccountType(String account_type) {
		this.account_type = account_type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account_type, amount, clientId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddOrEditAccountDTO other = (AddOrEditAccountDTO) obj;
		return Objects.equals(account_type, other.account_type) && amount == other.amount && clientId == other.clientId;
	}
	
	
	
}
