package it.edu.maxplanck.gpoProject_Server_WS.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server_WS.auth.services.AuthentificationServices;
import it.edu.maxplanck.gpoProject_Server_WS.database.services.DatabaseServices;

@RestController
@RequestMapping("api/v1")
public class EndpointService {

	private final AuthentificationServices authentificationServices;
	private final DatabaseServices databaseServices;
	
	public EndpointService(AuthentificationServices authentificationServices, DatabaseServices databaseServices) {
		super();
		this.authentificationServices = authentificationServices;
		this.databaseServices = databaseServices;
	}

	public AuthentificationServices getAuthentificationServices() {
		return authentificationServices;
	}

	public DatabaseServices getDatabaseServices() {
		return databaseServices;
	}
}
