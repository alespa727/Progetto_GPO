package it.edu.maxplanck.gpoProject_Server.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
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
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAuth;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseProfileDTO;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services/")
public class ServiceApiAccountController extends BasicApiRestController {

	public ServiceApiAccountController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}
	
	//--------------------------------------------------------------------------------------------------------------
	
	/**
	 * Cerca una immagine
	 * @param u
	 * @throws IOException
	 */
	private void findImage(User u) throws IOException {
	    if (u == null || u.getImagePath() == null || u.getImagePath().isBlank()) {
	        return;
	    }

	    Path imagePath = Paths.get(GenericUtil.getUploadDir()).resolve(u.getImagePath());

	    if (!Files.exists(imagePath)) {
	        // immagine persa → reset DB
	        this.databaseService.updateUserProfile(u.getPkID(), null);
	        throw new IllegalArgumentException("Immagine non trovata");
	    }
	}

	/**
	 * Controlla se il file e' una immagine
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private boolean isFileValidImage(MultipartFile file) throws IOException {
	    Tika tika = new Tika();
	    String detectedType = tika.detect(file.getInputStream());
	    return detectedType.startsWith("image/");
	}
	
	/**
	 * Crea una cartella nel path
	 * @param uploadPath
	 * @throws IOException
	 */
	private void createDirectory(Path uploadPath) throws IOException {
	    Files.createDirectories(uploadPath);
	}
	
	/**
	 * Salva il file
	 * @param uploadPath
	 * @param imageBytes
	 * @param fileName
	 * @throws IOException
	 */
	private void saveFile(Path uploadPath, byte[] imageBytes, String fileName) throws IOException {
	    Path filePath = uploadPath.resolve(fileName);

	    Files.copy(
	        new ByteArrayInputStream(imageBytes),
	        filePath,
	        StandardCopyOption.REPLACE_EXISTING
	    );
	}
	
	/**
	 * Elimina il file se esiste
	 * @param uploadPath
	 * @param fileName
	 * @throws IOException
	 */
	private void removeFile(Path uploadPath, String fileName) throws IOException {
	    Files.deleteIfExists(uploadPath.resolve(fileName));
	}
	
	//--------------------------------------------------------------------------------------------------------------
	
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

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("File vuoto");
	    }

	    /* Autenticazione */
	    int id;
	    try {
	        ResponseAuth r = this.auth(request, response);
	        
	        if (r == null || r.id() == null) throw new IllegalArgumentException();
	        id = r.id();
	        
	        if (r.access() != null) response.addCookie(r.access());
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();
	    }

	    /* User */
	    User u;
	    try {
	        u = this.databaseService.findUser(id);
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().build();
	    }

	    /* Verifica immagine */
	    try {
	        if (!isFileValidImage(file)) return ResponseEntity.badRequest().body("File non immagine");
	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }

	    /* Genera JPG quadrato */
	    byte[] imageBytes;
	    try {
	        imageBytes = GenericUtil.makeSquare(file.getInputStream());
	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }

	    /* Nome file deterministico */
	    String fileName = "imageProfile_" + u.getUsername() + GenericUtil.generateString(30, GenericUtil.CHARSET) + ".jpg";
	    Path uploadPath = Paths.get(GenericUtil.getUploadDir());

	    try {
	        createDirectory(uploadPath);

	        // elimina vecchia immagine
	        if (u.getImagePath() != null) removeFile(uploadPath, u.getImagePath());

	        saveFile(uploadPath, imageBytes, fileName);

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }
	    
	    try {
	    	this.databaseService.updateUserProfile(id, fileName);
	    }catch(IllegalArgumentException e) {
	    	return ResponseEntity.internalServerError().build();
	    }

	    return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene i dati del profilo dell'account in uso
	 * @param request
	 * @param response
	 * @return
	 */
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
		
		// Controllo se la immagine esiste o e' stata eliminata/persa
		try {
			if(u.getImagePath() != null) this.findImage(u);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.internalServerError().build();
		}
		
		/*
		 * Ritorna dati
		 */
		ResponseProfileDTO r = new ResponseProfileDTO((u.getImagePath() != null)? GenericUtil.standardPathImages + u.getImagePath() : null);
		
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
		for(User u : listFriends) {
			String imagePath = u.getImagePath();
			try{
				this.findImage(u);
			}catch(Exception e) {
				imagePath = null;
			}
			f.add(new ResponseFriendDTO(u.getUsername(), imagePath));
		}
		
		ResponseFriendsDTO friends = new ResponseFriendsDTO(f);
		return ResponseEntity.ok().body(friends);
	}
}
