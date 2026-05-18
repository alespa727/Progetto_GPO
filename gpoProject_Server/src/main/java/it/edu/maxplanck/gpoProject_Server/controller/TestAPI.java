package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rest Controller che contiene gli endpoint per i servizi di test
 */
@RestController
@RequestMapping("api/test")
public class TestAPI extends BasicApiRestController {
	
	public TestAPI(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}

    @PostMapping("registration")
    public ResponseEntity<?> registration(HttpServletRequest request, HttpServletResponse response) {

        /*
         * Prova a creare un nuovo utente:
         * 		- Creazione fallisce -> Errore database / dati inseriti non validi
         */
        User u = this.databaseService.getUsersRepo().findUserByUsername("ale");
        u.setAdmin(true);
        this.databaseService.getUsersRepo().save(u);

        return ResponseEntity.ok().body(Map.of("message", "Resa admin avvenuta con successo"));
    }
}
