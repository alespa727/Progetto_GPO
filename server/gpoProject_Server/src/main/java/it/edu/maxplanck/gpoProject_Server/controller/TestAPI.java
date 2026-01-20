package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.TestUserDTO;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/test")
public class TestAPI extends BasicApiRestController {

	private TokenService tokenService;
	private CookieService cookieService;
	private DatabaseService databaseService;

	public TestAPI(TokenService tokenService, CookieService cookieService, DatabaseService databaseService) {
		super();
		this.tokenService = tokenService;
		this.cookieService = cookieService;
		this.databaseService = databaseService;
	}

	// Test token
	@GetMapping("token")
	public String testToken() {
		String token = "";

		String subject = "";
		Map<String, Object> claims = new HashMap<String, Object>();

		claims.put("user1", "password1");
		claims.put("user2", "password2");

		token = this.tokenService.getTokenAccess(subject, claims);

		return token;
	}

	@GetMapping("cookies")
	public ResponseEntity<String> testCookies(HttpServletResponse response) {

		ResponseEntity<String> respons;

		String token = "Token access";

		String subject = "Token";
		// if(subject == null) return respons = new
		// ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

		Map<String, Object> claims = new HashMap<String, Object>();

		claims.put("username", "ale");
		claims.put("password", "password");
		// if(claims == null) return respons = new
		// ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

		token = this.tokenService.getTokenAccess(subject, claims);
		// if(token == null) return respons = new
		// ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

		String name = "CookieTest";
		Cookie cookie = this.cookieService.generateCookie(name, token, true, false, "/api/",
				15 * UtilServer.timeExpirationDateCookieToken);

		response.addCookie(cookie);

		respons = new ResponseEntity<String>(HttpStatus.OK);

		return respons;
	}

	@GetMapping("profile")
	public String testProfile(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = this.cookieService.getCookie(request.getCookies(), "CookieTest");
		// if(cookie == null) return "Cookie scaduto";

		Claims claims = this.tokenService.getClaimsAccess(cookie.getValue());
		// if(claims == null) return "Claim non ottenuti";

		String name = claims.get("username", String.class);
		// if(name == null) return "Username non esistente";

		String password = claims.get("password", String.class);
		// if(password == null) return "Password non esistente";

		return "Utente: " + name + " " + password;
	}

	@PostMapping("createUser")
	public ResponseEntity<String> testPostUser(@RequestBody TestUserDTO u) {

		if (u.username() == null || u.username().isBlank()) {
			return ResponseEntity.badRequest().body("Username obbligatorio");
		}

		if (u.password() == null || u.password().isBlank()) {
			return ResponseEntity.badRequest().body("Password obbligatoria");
		}

		try {
			databaseService.createUser(new User(null, u.username(), u.password(), u.isAdmin(), null, null, u.imagePath()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok("Creazione avvenuta");
	}

	@GetMapping("getUser")
	public ResponseEntity<User> testGetUser(@RequestParam String username) {

	    User user = databaseService.getUsersRepo().findByUsername(username);

	    if (user == null) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok(user);
	}

	@DeleteMapping("deleteUser")
	public String testDeleteUser(@RequestBody TestUserDTO u) {

		User user = this.databaseService.getUsersRepo().findByUsername(u.username());

		if (user == null) {
			return "user non trovato";
		}

		try {
			this.databaseService.deleteUser(user);
		} catch (NullPointerException e) {
			return e.getMessage();
		}

		return "Eliminazione avvenuta";
	}
}
