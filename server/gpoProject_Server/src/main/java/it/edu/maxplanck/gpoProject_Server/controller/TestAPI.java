package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;

@RestController
@RequestMapping("api/test")
public class TestAPI extends BasicApiRestController {

	public TestAPI(
			TokenService tokenService, 
			CookieService cookieService, 
			DatabaseService databaseService,
			@Value(UtilServer.accessTokenSubjectPath) String accessTokenSubject, 
			@Value(UtilServer.accessCookiePath) String accessCookieName, 
			@Value(UtilServer.refreshTokenSubjectPath) String refreshTokenSubject,
			@Value(UtilServer.refreshCookiePath) String refreshCookieName
		) {
		super(tokenService, cookieService, databaseService, accessTokenSubject, accessCookieName, refreshTokenSubject, refreshCookieName);
	}
}
