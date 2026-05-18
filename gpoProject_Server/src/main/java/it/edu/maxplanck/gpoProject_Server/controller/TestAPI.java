package it.edu.maxplanck.gpoProject_Server.controller;

import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccessDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;

import java.util.Map;

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
