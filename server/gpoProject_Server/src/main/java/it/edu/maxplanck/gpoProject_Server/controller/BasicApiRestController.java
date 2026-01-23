package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;

@RequestMapping("api")
public abstract class BasicApiRestController {
	
	protected final DatabaseService databaseService;
	protected final AuthenticationService authenticationService;
	
	public BasicApiRestController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super();
		this.databaseService = databaseService;
		this.authenticationService = authenticationService;
	}

	@GetMapping("")
	public String HelloWorld() {
		return "Hello world form api!";
	}
}
