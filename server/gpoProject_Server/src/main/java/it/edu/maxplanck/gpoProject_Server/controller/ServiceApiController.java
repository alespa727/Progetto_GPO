package it.edu.maxplanck.gpoProject_Server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.*;
import it.edu.maxplanck.gpoProject_Server.dto.response.*;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services")
public class ServiceApiController extends BasicApiRestController {
	
	@Value("${app.upload.dir}")
	private String uploadDir;
	
	public ServiceApiController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}

	// -----------------------------------------------------------------------
	
	@GetMapping("account")
	public ResponseEntity<?> getAccount(HttpServletRequest request, HttpServletResponse response) {
		
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
		 * Prendi user da database attraverso id
		 */
		User u = null;
		try{
			u = this.databaseService.findUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		
		/*
		 * Ritorna dati
		 */
		ResponseAccountDTO responseDTO = new ResponseAccountDTO(u.getUsername(), u.isAdmin(), u.getCreatedAt(), u.getImagePath());
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PatchMapping("account")
	public ResponseEntity<?> patchAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccountDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authAccountDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Update dati in database attraverso id
		 */
		try{
			this.databaseService.updateUserAccount(id, body.username(), body.password());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("account")
	public ResponseEntity<?> deleteAccount(HttpServletRequest request, HttpServletResponse response) {

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
		 * Elimina dati in database attraverso id
		 */
		this.databaseService.getUsersRepo().deleteById(id);
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("profile")
	public ResponseEntity<?> getProfilo(HttpServletRequest request, HttpServletResponse response) {

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
		 * Prendi user da database attraverso id
		 */
		User u;
		try {
			u = this.databaseService.findUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		/*
		 * Ritorna dati
		 */
		ResponseProfileDTO r = new ResponseProfileDTO((u.getImagePath() != null)? GenericUtil.standardPathImages + u.getImagePath() : null);
		
		return ResponseEntity.ok().body(r);
	}

	@PatchMapping("profileImage")
	public ResponseEntity<String> patchImageProfile(HttpServletRequest request, HttpServletResponse response, @RequestParam("image") MultipartFile file) {

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("File vuoto");
	    }

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
		 * Prendi user da database attraverso id
		 */
		User u = null;
		try{
			u = this.databaseService.findUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		
		/*
		 * Modifica nome immagine
		 */
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		
		if(!extension.equals("jpg") || !extension.equals("png")) return ResponseEntity.badRequest().body("Non un file jpg o png");
		
		String fileName = "imageProfile_" + u.getUsername() + extension;
		Path uploadPath = Paths.get(uploadDir);

	    /*
	     * Controllo se cartella dove salvare esiste o no -> la crea
	     */
	    if (!Files.exists(uploadPath)) {
	        try{
	        	Files.createDirectories(uploadPath);
	        }catch(IOException e) {
	        	return ResponseEntity.internalServerError().build();
	        }
	    }

	    /*
	     * Prova a salvare l'immagine nella cartella
	     */
	    Path filePath = uploadPath.resolve(fileName);
	    try{
	    	Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	    }catch(IOException e) {
	    	return ResponseEntity.internalServerError().build();
	    }
	    
    	/*
		 * Update dati in database attraverso id
		 */
		try{
			this.databaseService.updateUserProfile(id, fileName);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
		
	    return ResponseEntity.ok().build();
	}

	@PostMapping("friend")
	public ResponseEntity<?> postFriend(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestFriendDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authFriendDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Ottiene dati da database attraverso id
		 */
		try{
			this.databaseService.createFriendship(id, body.username());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("friends")
	public ResponseEntity<?> getFriends(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene dati da database attraverso id
		 */
		List<User> listFriends;
		try{
			listFriends = this.databaseService.findFriendsOfUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		/*
		 * Ritorna dati
		 */
		List<ResponseFriendDTO> f = new ArrayList<ResponseFriendDTO>();
		for(User u : listFriends) f.add(new ResponseFriendDTO(u.getUsername(), u.getImagePath()));
		
		ResponseFriendsDTO friends = new ResponseFriendsDTO(f);
		return ResponseEntity.ok().body(friends);
	}

	@PostMapping("chat")
	public ResponseEntity<?> postChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChatDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authChatDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea una nuova chat in database
		 */
		try{
			this.databaseService.createChat(id, body.friend().username());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("community")
	public ResponseEntity<?> postCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authCommunityDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea una nuova community in database
		 */
		try{
			this.databaseService.createCommunity(id, body.isInviteCodeValid(), body.name(), body.description());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
	
		return ResponseEntity.created(null).build();
	}

	@PostMapping("/{chat}/message")
	public ResponseEntity<?> postMessageChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId, @RequestBody RequestMessageChatDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authMessageChatDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea un messaggio in una determinata chat
		 */
		try {
			this.databaseService.createMessageChat(id, chatId, body.message());
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("messageCommunity")
	public ResponseEntity<?> postMessageCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestMessageCommunityDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authMessageCommunityDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea un messaggio in una determinata community
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("callChat")
	public ResponseEntity<?> postCallChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCallDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authCallDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea una nuova chiamata nel database nella chat
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("sectionCommunity")
	public ResponseEntity<?> postSection(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestSectionDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authSectionDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea una nuova sezione in una community in database
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("channelSection")
	public ResponseEntity<?> postChannel(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChannelDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		try {
			this.authenticationService.getAuthenticationRequestDTOService().authChannelDTO(body);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
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
		 * Crea un nuovo canale in una sezione di una community in database
		 */
		
		return ResponseEntity.created(null).build();
	}

	@GetMapping("chats")
	public ResponseEntity<?> getChats(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene le varie chat dell'utente
		 */
		List<Chat> listChats = null;
		try {
			listChats = this.databaseService.findChatsOfUser(id);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
		
		List<ResponseChatDTO> c = new ArrayList<ResponseChatDTO>();
		for(Chat ch : listChats) {
			User u = (ch.getFkFriendship().getFkUser1().getPkID() == id)? ch.getFkFriendship().getFkUser2() : ch.getFkFriendship().getFkUser1();
			c.add(new ResponseChatDTO(ch.getPkID(), new ResponseFriendDTO(u.getUsername(), GenericUtil.standardPathImages + u.getImagePath())));
		}
		
		ResponseChatsDTO chats = new ResponseChatsDTO(c);
		return ResponseEntity.ok().body(chats);
	}
	
	@GetMapping("chat")
	public ResponseEntity<?> getChatData(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene i dati di una chat
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("community")
	public ResponseEntity<?> getCommunity(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene i dati di una community
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping("chat")
	public ResponseEntity<?> deleteChat(HttpServletRequest request, HttpServletResponse response) {

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
		 * Elimina una chat
		 */
		
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("community")
	public ResponseEntity<?> deleteCommunity(HttpServletRequest request, HttpServletResponse response) {

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
		 * Elimina una community
		 */
		
		return ResponseEntity.ok().build();
	}

	// -----------------------------------------------------------------------
	
	@GetMapping("messageChat")
	public ResponseEntity<?> getMessageChat(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene i messaggi di una chat
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("callChat")
	public ResponseEntity<?> getDatiCall(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene le chiamate di una chat
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("usersCommunity")
	public ResponseEntity<?> getUserCommunity(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene gli user della community
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("sectionCommunity")
	public ResponseEntity<?> getSections(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene tutte le sezioni della community
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("channelsSection")
	public ResponseEntity<?> getChannels(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene tutte i canali della sezione
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("messagesChannel")
	public ResponseEntity<?> getMessagesChannel(HttpServletRequest request, HttpServletResponse response) {

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
		 * Ottiene tutti i messaggi della community
		 */
		
		return ResponseEntity.ok().body(null);
	}
}
