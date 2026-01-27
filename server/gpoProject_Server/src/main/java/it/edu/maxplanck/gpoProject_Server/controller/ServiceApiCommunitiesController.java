package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSectionDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services")
public class ServiceApiCommunitiesController extends BasicApiRestController {

	public ServiceApiCommunitiesController(DatabaseService databaseService,
			AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Crea una community come owner l'utente
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
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

	/**
	 * Ottiene i dati di una community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @return
	 */
	@GetMapping("communities/{community}/data")
	public ResponseEntity<?> getDataCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId) {

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
	
	/**
	 * Ottiene gli users che fanno parte della community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @return
	 */
	@GetMapping("communities/{community}/users")
	public ResponseEntity<?> getUsersCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId) {

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
	
	/**
	 * Ottiene tutte le community di quali faccio parte
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("communities")
	public ResponseEntity<?> getCommunities(HttpServletRequest request, HttpServletResponse response){
		return null;
	}

	/**
	 * Elimina la community/l'iscrizione alla community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @return
	 */
	@DeleteMapping("communities/{community}")
	public ResponseEntity<?> deleteCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId) {

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
	
	/**
	 * Crea una nuova sezione nella community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @param body
	 * @return
	 */
	@PostMapping("communities/{community}/section")
	public ResponseEntity<?> postSection(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId, @RequestBody RequestSectionDTO body) {

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

	/**
	 * Ottiene tutte le sezioni della community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @return
	 */
	@GetMapping("communities/{community}/sections")
	public ResponseEntity<?> getSections(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId) {

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

	/**
	 * Crea un canale nella sezione della community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @param sectionId
	 * @param body
	 * @return
	 */
	@PostMapping("communities/{community}/sections/{section}/channel")
	public ResponseEntity<?> postChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId, @PathVariable("section") Integer sectionId, @RequestBody RequestChannelDTO body) {

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
	
	/**
	 * Ottiene tutti canali della community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @param sectionId
	 * @return
	 */
	@GetMapping("communities/{community}/sections/{section}/channels")
	public ResponseEntity<?> getChannels(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId, @PathVariable("section") Integer sectionId) {

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

	/**
	 * Crea un messaggio in un canale della sezione della community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @param sectionId
	 * @param channelId
	 * @param body
	 * @return
	 */
	@PostMapping("communities/{community}/sections/{section}/channels/{channel}/message")
	public ResponseEntity<?> postMessageCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId, @PathVariable("section") Integer sectionId, @PathVariable("channel") Integer channelId, @RequestBody RequestMessageCommunityDTO body) {

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
	
	/**
	 * Ottiene i messaggi del canale della community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @param sectionId
	 * @param channelId
	 * @return
	 */
	// GET /communities/{community}/channels/{channel}/messages?message=123
	@GetMapping("communities/{community}/channels/{channel}/messages")
	public ResponseEntity<?> getMessagesChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer commmunityId, @PathVariable("section") Integer sectionId, @PathVariable("channel") Integer channelId, @RequestParam("message") Integer messageId) {

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
