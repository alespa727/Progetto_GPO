package it.edu.maxplanck.gpoProject_Server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestProfileDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseProfileDTO;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rest Controller che contiene gli endpoint per i servizi forniti dall'account
 */
@RestController
@RequestMapping("api/services/")
public class ServiceApiAccountController extends BasicApiRestController {
	
	public ServiceApiAccountController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}
	
	//--------------------------------------------------------------------------------------------------------------
	
	/**
	 * Modifica i dati dell'account in uso
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PatchMapping("account")
	public ResponseEntity<?> patchAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccountDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authAccountDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Update dati in database attraverso id
		 */
		this.databaseService.updateUserAccount(id, body.username(), body.password());
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene i dati dell'account usando i dati salvati nel cookie
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("account")
	public ResponseEntity<?> getAccount(HttpServletRequest request, HttpServletResponse response) {
		
		/*
		 * Autentificazione
		 */
        Integer id;
        try{
           id = this.authenticationService.authenticate(request, response);
        }catch(Exception e){
            return ResponseEntity.ok().build();
        }

		
		/*
		 * Prendi user da database attraverso id
		 */
		User u = this.databaseService.findUser(id);
		
		String image = (u.getImagePath() == null)? null : this.standardServerPath + this.standardPathImages + u.getImagePath();
		/*
		 * Ritorna dati
		 */
		ResponseAccountDTO responseDTO = new ResponseAccountDTO(u.getUsername(), u.isAdmin(), u.getCreatedAt(), image);
		return ResponseEntity.ok().body(responseDTO);
	}

	/**
	 * Elimina l'account in uso
	 * @param request
	 * @param response
	 * @return
	 */
	@DeleteMapping("account")
	public ResponseEntity<?> deleteAccount(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Autentificazione
		 */

		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Elimina dati in database attraverso id
		 */
		this.databaseService.getUsersRepo().deleteById(id);
		
		/*
		 * Rimozione cookies
		 */
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, "", true, false, "/api/", 0));
		response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, "", true, false, "/api/", 0));
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Modifica i dati del profilo dell'account in uso
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PatchMapping("profile")
	public ResponseEntity<?> patchProfile(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestProfileDTO body){
		
		/*
		 * Autentificazione
		 */
		this.authenticationService.authenticate(request, response);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Modifica l'immagine profilo dell'account in uso
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@PatchMapping("profileImage")
	public ResponseEntity<?> patchImageProfileImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("image") MultipartFile file) {

	    if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "File vuoto"));

	    /*
	     * Autenticazione
	     */
	    int id = this.authenticationService.authenticate(request, response);
	    User u = this.databaseService.findUser(id);

	    /*
	     * Verifica immagine
	     */
	    try {
	        if (!GenericUtil.isFileValidImage(file)) return ResponseEntity.badRequest().body(Map.of("message", "File non immagine"));
	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }

	    /* 
	     * Genera JPG quadrato
	     */
	    byte[] imageBytes;
	    try {
	        imageBytes = GenericUtil.makeSquare(file.getInputStream());
	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }

	    /* 
	     * Nome file da salvare
	     */
	    String fileName = null;
	    Path uploadPath = Paths.get(this.uploadDirImages);
	    
	    try {
	        GenericUtil.createDirectory(uploadPath);

	        // elimina vecchia immagine
	        if (u.getImagePath() != null) GenericUtil.removeFile(uploadPath, u.getImagePath());
	        
	        do {
		    	fileName = GenericUtil.generateString(((int) UtilDatabase.UserData.imagePathLenght / 2), GenericUtil.CHARSET) + ".jpg";
		    }while(Files.exists(uploadPath.resolve(fileName)));
	        
	        // Salva nuova immagine
	        GenericUtil.saveFile(uploadPath, imageBytes, fileName);

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }
	    
	    this.databaseService.updateUserProfile(id, fileName);

	    return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene i dati del profilo dell'account in uso
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("profile")
	public ResponseEntity<?> getProfile(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Prendi user da database attraverso id
		 */
		User u = this.databaseService.findUser(id);
		
		String image = this.findImage(u);
		
		/*
		 * Ritorna dati
		 */
		ResponseProfileDTO r = new ResponseProfileDTO(image);
		
		return ResponseEntity.ok().body(r);
	}
	
	/**
	 * Aggiungi amico dell'account in uso
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
    @PostMapping("friend")
    public ResponseEntity<?> postFriend(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestFriendDTO body) {
        try {
            // Controlla validità body
            this.authenticationService.getAuthenticationRequestDTOService().authFriendDTO(body);

            // Autentificazione
            int id = this.authenticationService.authenticate(request, response);


            User user = this.databaseService.findUser(id);

            if(user.getUsername().equals(body.username())) return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Non puoi inviare la richiesta a te stesso"));

            // Crea amicizia
            this.databaseService.createFriendship(id, body.username());

            // Crea chat
            this.databaseService.createChat(id, body.username());

            return ResponseEntity.ok().body(Map.of("message", "Amicizia creata con successo"));

        } catch (DatabaseException e) {

            return ResponseEntity
                    .status(e.getExceptions().getResponseStatus())
                    .body(Map.of("message", e.getExceptions().getMessage()));
        }
    }


    /**
	 * Ottiene gli amici dell'account in uso
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("friends")
	public ResponseEntity<?> getFriends(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene dati da database attraverso id
		 */
		List<User> listFriends = this.databaseService.findFriendsOfUser(id);
		if(listFriends == null || listFriends.isEmpty()) return ResponseEntity.ok().body(new ResponseFriendsDTO(null));
		
		/*
		 * Ritorna dati
		 */
		List<ResponseFriendDTO> f = new ArrayList<ResponseFriendDTO>();
		for(User u : listFriends) {
			
			String image = this.findImage(u);
			f.add(new ResponseFriendDTO(u.getUsername(), image));
		}
		
		ResponseFriendsDTO friends = new ResponseFriendsDTO(f);
		return ResponseEntity.ok().body(friends);
	}
}
