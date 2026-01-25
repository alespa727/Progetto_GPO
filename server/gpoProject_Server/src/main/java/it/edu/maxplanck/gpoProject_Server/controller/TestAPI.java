package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;

@RestController
@RequestMapping("api/test")
public class TestAPI extends BasicApiRestController {
	
	public TestAPI(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}
}
