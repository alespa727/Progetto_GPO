package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/admin")
public class AdminApiController extends BasicApiRestController {

	public AdminApiController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Aggiorna i dati dello status
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("status")
	public ResponseEntity<?> status(HttpServletRequest request, HttpServletResponse response, @PathVariable("isOnline") boolean status){
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		User u = this.databaseService.findUser(id);
		if(!u.isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		/*
		 * Update database
		*/
		this.databaseService.updateStatusUser(u, status);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene le immagini profilo degli utenti richiesti
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PostMapping("profiles")
	public ResponseEntity<?> users(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestFriendsDTO body){
		
		int id = this.authenticationService.authenticate(request, response);
		
		User u = this.databaseService.findUser(id);
		if(!u.isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		List<ResponseFriendDTO> usersImagePaths = new ArrayList<ResponseFriendDTO>();
		for(RequestFriendDTO r : body.users()) {
			User utente = null;
			try{
				utente = this.databaseService.findUser(r.username());
			} catch(Exception e) {
				e.printStackTrace();
			}
			usersImagePaths.add(new ResponseFriendDTO(r.username(), this.findImage(utente)));
		}
		
		ResponseFriendsDTO users = new ResponseFriendsDTO(usersImagePaths);
		
		return ResponseEntity.ok().body(users);
	}
}
