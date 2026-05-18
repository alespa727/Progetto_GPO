package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccessDTO;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import it.edu.maxplanck.gpoProject_Server.exceptions.TokenException;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rest Controller che contiene gli endpoint per i servizi di start
 */
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
        try{
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
        }catch(DatabaseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Utente già esistente"));
        }


		return ResponseEntity.ok().body(Map.of("message", "Registrazione avvenuta con successo"));
	}

	/**
	 * Permette di fare il login
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
    @PostMapping("login")
    public ResponseEntity<?> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody(required = false) RequestAccessDTO body
    ) {
        // Controllo se c'è un refresh token valido
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (UtilServer.refreshCookieName.equals(c.getName())) {
                    refreshToken = c.getValue();
                    break;
                }
            }
        }

        // Se esiste un refresh token valido -> login automatico
        if (refreshToken != null) {
            try {
                Claims claims = this.authenticationService.getTokenService().getClaimsRefresh(refreshToken);

                // genera nuovo access token
                String accessToken = this.authenticationService
                        .getTokenService()
                        .generateTokenAccess(claims, UtilServer.accessTokenSubject);

                // genera nuovo cookie access token
                Cookie access = this.authenticationService
                        .getCookieService()
                        .generateCookie(UtilServer.accessCookieName, accessToken, true, false,
                                "/api/", UtilServer.timeExpirationDateAccessCookie);
                response.addCookie(access);

                return ResponseEntity.ok(Map.of(
                        "message", "Login automatico avvenuto con refresh token valido",
                        "accessToken", accessToken
                ));
            } catch (TokenException e) {
                // refresh token non valido → prosegui al login normale
            }
        }

        // Login normale con username/password
        if (body == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Nessun refresh token valido e nessun body login fornito"));
        }

        this.authenticationService.getAuthenticationRequestDTOService().authAccessDTO(body);
        int id = this.databaseService.findUser(body.username(), body.password());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", body.username());

        String accessToken = this.authenticationService
                .getTokenService()
                .generateTokenAccess(claims, UtilServer.accessTokenSubject);

        Cookie access = this.authenticationService
                .getCookieService()
                .generateCookie(UtilServer.accessCookieName, accessToken, true, false,
                        "/api/", UtilServer.timeExpirationDateAccessCookie);

        String refreshTokenNew = this.authenticationService
                .getTokenService()
                .generateTokenRefresh(claims, UtilServer.refreshTokenSubject);

        Cookie refresh = this.authenticationService
                .getCookieService()
                .generateCookie(UtilServer.refreshCookieName, refreshTokenNew, true, false,
                        "/api/", UtilServer.timeExpirationDateRefreshCookie);

        response.addCookie(access);
        response.addCookie(refresh);

        return ResponseEntity.ok(Map.of(
                "message", "Login avvenuto con successo",
                "accessToken", accessToken
        ));
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
		int id = this.authenticationService.authenticate(request, response);
		this.databaseService.findUser(id);
		
		/*
		 * Rimozione cookies
		 */
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, "", true, false, "/api/", 0));
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, "", true, false, "/api/", 0));
		
		return ResponseEntity.ok().build();
	}
    @PostMapping("services/isUsernameFree")
    public ResponseEntity<?> isUsernameFree(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("username") String username) {

        int id = this.authenticationService.authenticate(request, response);
        this.databaseService.findUser(id);

        boolean isUsed = this.databaseService.doesUserExist(username) != null;

        return ResponseEntity.ok(!isUsed);
    }
}
