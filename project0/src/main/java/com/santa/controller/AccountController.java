package com.santa.controller;

import java.util.List;

import com.santa.dto.AddOrEditAccountDTO;
import com.santa.model.Account;
import com.santa.service.AccountService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController implements Controller{

	private AccountService accountService;
	
	public AccountController() {
		this.accountService = new AccountService();
	}
	
	private Handler getAccountFromClient = (ctx) -> {
		String clientId = ctx.pathParam("clientid");
		String upperEnd = ctx.queryParam("amountLessThan");
		String lowerEnd = ctx.queryParam("amountGreaterThan");
		
		List<Account> accountsFromClient = accountService.getAllAccountsFromClient(clientId, lowerEnd, upperEnd);
		
		ctx.json(accountsFromClient);
	};
	
	
	public Handler getSpecificAccountFromClient = (ctx) ->{
		String clientId = ctx.pathParam("clientid");
		String accountId = ctx.pathParam("accountid");
		
		Account accountFromClient = accountService.getSpecificAccountFromClient(clientId, accountId);
		
		ctx.json(accountFromClient);
	};
	
	private Handler addAccountForClient = (ctx) -> {
		AddOrEditAccountDTO accountToAdd = ctx.bodyAsClass(AddOrEditAccountDTO.class);

		String clientId = ctx.pathParam("clientid");
		Account addedAccount = accountService.addAccountForClient(accountToAdd, clientId);
		ctx.json(addedAccount);
	};
	
	private Handler updateSpecificAccountForClient = (ctx) -> {
		AddOrEditAccountDTO accountToAdd = ctx.bodyAsClass(AddOrEditAccountDTO.class);
		
		String clientId = ctx.pathParam("clientid");
		String accountId = ctx.pathParam("accountid");
		Account addedAccount = accountService.updateSpecificAccountForClient(clientId, accountId, accountToAdd);
		ctx.json(addedAccount);
	};
	
	private Handler deleteAccount = (ctx) -> {
		String clientId = ctx.pathParam("clientid");
		String accountId = ctx.pathParam("accountid");
		
		accountService.deleteAccount(clientId, accountId);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/clients/:clientid/accounts", addAccountForClient);
		app.get("/clients/:clientid/accounts", getAccountFromClient);
		app.get("/clients/:clientid/accounts/:accountid", getSpecificAccountFromClient);
		app.put("/clients/:clientid/accounts/:accountid", updateSpecificAccountForClient);
		app.delete("/clients/:clientid/accounts/:accountid", deleteAccount);
	}

}
