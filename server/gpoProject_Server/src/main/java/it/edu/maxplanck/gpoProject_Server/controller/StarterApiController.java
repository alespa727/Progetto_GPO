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
		this.authenticationService.getAuthenticationRequestDTOService().authAccessDTO(body);
		
		/*
		 * Prova a creare un nuovo utente:
		 * 		- Creazione fallisce -> Errore database / dati inseriti non validi
		*/
		this.databaseService.createUser(body.username(), body.password());

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
		this.authenticationService.getAuthenticationRequestDTOService().authAccessDTO(body);
		
		/*
		 * Prova a recuperare l'utente:
		 * 		- Creazione fallisce -> Errore database / dati inseriti non corretti
		*/
		int id  = this.databaseService.findUser(body.username(), body.password());
			
		/*
		 * Creazione risposta con token e cookies:
		 * 		- Creazione fallisce -> Errore interno
		*/
		Cookie access = null;
		Cookie refresh = null;
	
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("id", id);
		claims.put("username", body.username());
		
		String token = null;
		token = this.authenticationService.getTokenService().generateTokenAccess(claims, UtilServer.accessTokenSubject);
		access = this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, token, true, false, "/api/", UtilServer.timeExpirationDateAccessCookie);
	
		token = null;
		token = this.authenticationService.getTokenService().generateTokenRefresh(claims, UtilServer.refreshTokenSubject);
		refresh = this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, token, true, false, "/api/", UtilServer.timeExpirationDateRefreshCookie);
		
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
	public ResponseEntity<?> status(HttpServletRequest request, HttpServletResponse response){
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Update database
		*/
		LocalDateTime time = this.databaseService.updateStatusUser(id);
		
		return ResponseEntity.ok().body(time);
	}

	/**
	 * Fa il logout
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Update database
		*/
		this.databaseService.updateStatusUser(id);
		
		/*
		 * Rimozione cookies
		 */
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, "", true, false, "/api/", 0));
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, "", true, false, "/api/", 0));
		
		return ResponseEntity.ok().build();
	}
}
