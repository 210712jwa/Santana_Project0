package com.santa.dao;

import java.sql.SQLException;
import java.util.List;

import com.santa.dto.AddOrEditClientDTO;
import com.santa.model.Client;

public interface ClientDAO {
	
	public abstract List<Client> getAllClients() throws SQLException; 
	
	public abstract Client getClientById(int id) throws SQLException;
	
	public abstract Client addClient(AddOrEditClientDTO client) throws SQLException;
	
	public abstract Client editClient(int clientId, AddOrEditClientDTO client) throws SQLException;
	
	public abstract void deleteClient(int clientId) throws SQLException;
	
	
}
