package com.santa.controller;

import java.util.List;

import com.santa.dto.AddOrEditClientDTO;
import com.santa.model.Client;
import com.santa.service.ClientService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ClientController implements Controller {
	
	private ClientService clientService;

	public ClientController() {
		this.clientService = new ClientService();
	}

	private Handler getAllClients = (ctx) -> {	
		
		List<Client> clients = clientService.getAllClients();
		
		ctx.status(200);
		ctx.json(clients);
	};
	
	private Handler getClientById = (ctx) -> {
		String clientid = ctx.pathParam("clientid");
		
		Client client = clientService.getClientById(clientid);
		ctx.json(client);
	};
	
	private Handler addClient = (ctx) -> {
		AddOrEditClientDTO clientToAdd = ctx.bodyAsClass(AddOrEditClientDTO.class);
		
		Client addedClient = clientService.addClient(clientToAdd);
		ctx.json(addedClient);
	};
	
	private Handler editClient = (ctx) -> {
		AddOrEditClientDTO clientToEdit = ctx.bodyAsClass(AddOrEditClientDTO.class);
		
		String clientId = ctx.pathParam("clientid");
		Client editedClient = clientService.editClient(clientId, clientToEdit);
		
		ctx.json(editedClient);
	};
	
	private Handler deleteClient = (ctx) -> {
		String clientId = ctx.pathParam("clientid");
		
		clientService.deleteClient(clientId);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients", getAllClients);
		app.get("/clients/:clientid", getClientById);
		app.post("/clients", addClient);
		app.put("/clients/:clientid", editClient);
		app.delete("/clients/:clientid", deleteClient);
	}

}
