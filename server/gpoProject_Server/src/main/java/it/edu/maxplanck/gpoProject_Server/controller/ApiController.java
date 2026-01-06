package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.edu.maxplanck.gpoProject_Server.database.modelDB.Utente;
import it.edu.maxplanck.gpoProject_Server.database.service.ServiceDatabase;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestUserDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.BasicResponseDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseUserDTO;
import it.edu.maxplanck.gpoProject_Server.token.service.ServiceToken;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ServiceDatabase serviceDB;
    private final ServiceToken serviceToken;

    public ApiController(ServiceDatabase serviceDB, ServiceToken serviceToken) {
        this.serviceDB = serviceDB;
        this.serviceToken = serviceToken;
    }

    @GetMapping("hello")
    public String helloWorld() {
        return "Hello World from API!";
    }
    
    @GetMapping("/me")
    public ResponseEntity<BasicResponseDTO> me(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BasicResponseDTO("Authorization header mancante o malformato"));
        }

        String token = authHeader.substring(7); // rimuove "Bearer "
        
        if (!serviceToken.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BasicResponseDTO("Token non valido o scaduto"));
        }

        String username = serviceToken.extractSubject(token);
        boolean isAdmin = serviceToken.extractIsAdmin(token);

        return ResponseEntity.ok(new BasicResponseDTO("User: " + username + " | Admin: " + isAdmin));
    }

    // REGISTRAZIONE
    @PostMapping("registrazione")
    public ResponseEntity<ResponseUserDTO> register(@Valid @RequestBody RequestUserDTO account) {

        Utente saved = serviceDB.postUtente(account);

        ResponseUserDTO dto = new ResponseUserDTO(
                saved.getUsername(),
                saved.getCreatedAt()
        );

        String token = serviceToken.getTokenFromUser(account);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(dto);
    }

    // LOGIN
    @PostMapping("login")
    public ResponseEntity<ResponseUserDTO> login(@Valid @RequestBody RequestUserDTO account) {

        Utente saved = serviceDB.getUtente(account);

        ResponseUserDTO dto = new ResponseUserDTO(
                saved.getUsername(),
                saved.getCreatedAt()
        );

        String token = serviceToken.getTokenFromUser(account);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(dto);
    }
}
