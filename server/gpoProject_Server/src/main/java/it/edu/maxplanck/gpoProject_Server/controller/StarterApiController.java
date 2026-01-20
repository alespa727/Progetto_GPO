package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccessDTO;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class StarterApiController extends BasicApiRestController {

	public StarterApiController(
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

	/* ===================== REGISTRATION ===================== */
	@PostMapping("registration")
	public ResponseEntity<?> registration(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccessDTO body) {

		return null;
	}

	/* ===================== LOGIN ===================== */
	@PostMapping("login")
	public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccessDTO body) {
		
		return null;
	}

	/* ===================== TOKEN + COOKIE ===================== */
	protected void authenticateUser(HttpServletResponse response, User user) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getPkID());
		claims.put("username", user.getUsername());

		String accessToken = tokenService.getTokenAccess(accessTokenSubject, claims);
		String refreshToken = tokenService.getTokenAccess(refreshTokenSubject, claims);

		Cookie accessCookie = cookieService.generateCookie(accessCookieName, accessToken, true, false, "/api/", UtilServer.timeExpirationDateAccessCookie);

		Cookie refreshCookie = cookieService.generateCookie(refreshCookieName, refreshToken, true, false, "/api/", UtilServer.timeExpirationDateRefreshCookie);

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
	}
}
