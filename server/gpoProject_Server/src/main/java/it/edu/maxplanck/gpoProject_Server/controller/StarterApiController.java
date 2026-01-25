package it.edu.maxplanck.gpoProject_Server.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccessDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAuth;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class StarterApiController extends BasicApiRestController {

	public StarterApiController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Permette di fare la registrazione
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PostMapping("registration")
	public ResponseEntity<?> registration(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccessDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authAccessDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		/*
		 * Prova a creare un nuovo utente:
		 * 		- Creazione fallisce -> Errore database / dati inseriti non validi
		*/
		try{
			this.databaseService.createUser(body.username(), body.password());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}

	/**
	 * Permette di fare il login
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PostMapping("login")
	public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccessDTO body) {
		
		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authAccessDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		/*
		 * Prova a recuperare l'utente:
		 * 		- Creazione fallisce -> Errore database / dati inseriti non corretti
		*/
		int id;
		try {
			id = this.databaseService.findUser(body.username(), body.password());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
			
		/*
		 * Creazione risposta con token e cookies:
		 * 		- Creazione fallisce -> Errore interno
		*/
		Cookie access = null;
		Cookie refresh = null;
	
		try {
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("id", id);
			claims.put("username", body.username());
			
			String token = null;
			token = this.authenticationService.getTokenService().generateTokenAccess(claims, UtilServer.accessTokenSubject);
			access = this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, token, true, false, "/api/", UtilServer.timeExpirationDateAccessCookie);
		
			token = null;
			token = this.authenticationService.getTokenService().generateTokenRefresh(claims, UtilServer.refreshTokenSubject);
			refresh = this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, token, true, false, "/api/", UtilServer.timeExpirationDateRefreshCookie);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		response.addCookie(access);
		response.addCookie(refresh);

		return ResponseEntity.ok().build();
	}
	
	/**
	 * Aggiorna i dati se l'app viene chiusa
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("status")
	public ResponseEntity<?> postOpen(HttpServletRequest request, HttpServletResponse response){
		
		/*
		 * Autentificazione
		 */
		int id;
		try{
			ResponseAuth r = this.auth(request, response);
			
			if(r == null || r.id() == null) throw new IllegalArgumentException("Errore");			
			id = r.id();
			
			// Refresh access se non valido
			if(r.access() != null) response.addCookie(r.access());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		/*
		 * Update database
		*/
		LocalDateTime time;
		try {
			time = this.databaseService.updateStatusUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.ok().body(time);
	}

	/**
	 * Fa il logout
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("logout")
	public ResponseEntity<?> logoutAccount(HttpServletRequest request, HttpServletResponse response) {
		
		/*
		 * Autentificazione
		 */
		int id;
		try{
			ResponseAuth r = this.auth(request, response);
			
			if(r == null || r.id() == null) throw new IllegalArgumentException("Errore");			
			id = r.id();
			
			// Refresh access se non valido
			if(r.access() != null) response.addCookie(r.access());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		/*
		 * Update database
		*/
		try {
			this.databaseService.updateStatusUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		/*
		 * Rimozione cookies
		 */
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, "", true, false, "/api/", 0));
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, "", true, false, "/api/", 0));
		
		return ResponseEntity.ok().build();
	}
}
