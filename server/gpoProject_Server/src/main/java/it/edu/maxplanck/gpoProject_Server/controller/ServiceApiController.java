package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCallDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestProfileDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSectionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services")
public class ServiceApiController extends BasicApiRestController {
	
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
		
		/*
		 * Prendi il cookie -> Token -> id
		 */
		
		/*
		 * Prendi user da database attraverso id
		 */
		
		/*
		 * Ritorna dati
		 */
		
		return ResponseEntity.ok().body(null);
	}
	
	@PatchMapping("account")
	public ResponseEntity<?> patchAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccountDTO body) {
		
		/*
		 * Update dati in database attraverso id
		 */
		
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("account")
	public ResponseEntity<?> deleteAccount(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Elimina dati in database attraverso id
		 */
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("profile")
	public ResponseEntity<?> getProfilo(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Prendi user da database attraverso id
		 */
		
		/*
		 * Ritorna dati
		 */
		
		return ResponseEntity.ok().body(null);	
	}

	@PatchMapping("profile")
	public ResponseEntity<?> patchProfilo(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestProfileDTO body) {
		
		/*
		 * Update dati in database attraverso id
		 */
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("friends")
	public ResponseEntity<?> getFriends(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene dati da database attraverso id
		 */
		
		/*
		 * Ritorna dati
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@PostMapping("chat")
	public ResponseEntity<?> postChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChatDTO body) {
		
		/*
		 * Crea una nuova chat in database
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("community")
	public ResponseEntity<?> postCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body) {
		
		/*
		 * Crea una nuova community in database
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("messageChat")
	public ResponseEntity<?> postMessageChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestMessageChatDTO body) {
		
		/*
		 * Crea un messaggio in una determinata chat
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("messageCommunity")
	public ResponseEntity<?> postMessageCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestMessageCommunityDTO body) {
		
		/*
		 * Crea un messaggio in una determinata community
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("callChat")
	public ResponseEntity<?> postCallChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCallDTO body) {
		
		/*
		 * Crea una nuova chiamata nel database nella chat
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("sectionCommunity")
	public ResponseEntity<?> postSection(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestSectionDTO body) {
		
		/*
		 * Crea una nuova sezione in una community in database
		 */
		
		return ResponseEntity.created(null).build();
	}

	@PostMapping("channelSection")
	public ResponseEntity<?> postChannel(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChannelDTO body) {
		
		/*
		 * Crea un nuovo canale in una sezione di una community in database
		 */
		
		return ResponseEntity.created(null).build();
	}

	@GetMapping("chat")
	public ResponseEntity<?> getChat(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene i dati di una chat
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("community")
	public ResponseEntity<?> getCommunity(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene i dati di una community
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping("chat")
	public ResponseEntity<?> deleteChat(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Elimina una chat
		 */
		
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("community")
	public ResponseEntity<?> deleteCommunity(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Elimina una community
		 */
		
		return ResponseEntity.ok().build();
	}

	// -----------------------------------------------------------------------
	
	@GetMapping("messageChat")
	public ResponseEntity<?> getMessageChat(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene i messaggi di una chat
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("callChat")
	public ResponseEntity<?> getDatiCall(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene le chiamate di una chat
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("usersCommunity")
	public ResponseEntity<?> getUserCommunity(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene gli user della community
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("sectionCommunity")
	public ResponseEntity<?> getSections(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene tutte le sezioni della community
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("channelsSection")
	public ResponseEntity<?> getChannels(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene tutte i canali della sezione
		 */
		
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("messagesChannel")
	public ResponseEntity<?> getMessagesChannel(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Ottiene tutti i messaggi della community
		 */
		
		return ResponseEntity.ok().body(null);
	}
}
