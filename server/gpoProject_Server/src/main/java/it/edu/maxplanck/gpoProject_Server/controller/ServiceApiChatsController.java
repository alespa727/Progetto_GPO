package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.Call;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCallChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCallsChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChatsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessagesChatDTO;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services/")
public class ServiceApiChatsController extends BasicApiRestController {

	public ServiceApiChatsController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Crea una nuova chat con un amico
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PostMapping("chat")
	public ResponseEntity<?> postChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChatDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authChatDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Crea una nuova chat in database
		 */
		this.databaseService.createChat(id, body.friend().username());
		
		return ResponseEntity.created(null).build();
	}

	/**
	 * Ottiene tutte le chat create
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("chats")
	public ResponseEntity<?> getChats(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Ottiene le varie chat dell'utente
		 */
		List<Chat> listChats = this.databaseService.findChatsOfUser(id);
		
		List<ResponseChatDTO> c = new ArrayList<ResponseChatDTO>();
		for(Chat ch : listChats) {
			User u = (ch.getFkFriendship().getFkUser1().getPkID() == id)? ch.getFkFriendship().getFkUser2() : ch.getFkFriendship().getFkUser1();
			c.add(new ResponseChatDTO(ch.getPkID(), new ResponseFriendDTO(u.getUsername(), GenericUtil.standardPathImages + u.getImagePath())));
		}
		
		ResponseChatsDTO chats = new ResponseChatsDTO(c);
		return ResponseEntity.ok().body(chats);
	}
	
	/**
	 * Elimina una chat
	 * @param request
	 * @param response
	 * @param chatId
	 * @return
	 */
	@DeleteMapping("chats/{chat}")
	public ResponseEntity<?> deleteChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Elimina una chat
		 */
		this.databaseService.deleteChat(id, chatId);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Crea un nuovo messaggio nella chat
	 * @param request
	 * @param response
	 * @param chatId
	 * @param body
	 * @return
	 */
	@PostMapping("chats/{chat}/message")
	public ResponseEntity<?> postMessageChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId, @RequestBody RequestMessageChatDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authMessageChatDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Crea un messaggio in una determinata chat
		 */
		this.databaseService.createMessageChat(id, chatId, body.message());
		
		return ResponseEntity.created(null).build();
	}
	
	/**
	 * Ottiene un tot di messaggi della chat dal messaggio iniziale
	 * @param request
	 * @param response
	 * @param chatId
	 * @return
	 */
	// GET /chats/{chat}/messages -> default = 0
	// GET /chats/{chat}/messages?message=123
	@GetMapping("chats/{chat}/messages")
	public ResponseEntity<?> getMessagesChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId, @RequestParam(value = "message", required = false, defaultValue = "0") Integer messageId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Ottiene i messaggi di una chat
		 */
		List<MessageChat> messages = this.databaseService.getMessagesChat(id, chatId, messageId);
		
		List<ResponseMessageChatDTO> mess = new ArrayList<ResponseMessageChatDTO>();
		for(MessageChat m : messages) mess.add(new ResponseMessageChatDTO(m.getPkID(), m.getFkUser().getUsername(), m.getMessage(), m.getSentAt()));
		
		ResponseMessagesChatDTO m = new ResponseMessagesChatDTO(chatId, mess);
		
		return ResponseEntity.ok().body(m);
	}
	
	/**
	 * Crea una call in una chat
	 * @param request
	 * @param response
	 * @param chatId
	 * @param body
	 * @return
	 */
	@PostMapping("chats/{chat}/call")
	public ResponseEntity<?> postCallChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId) {
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Controlla se la chat esiste
		 */
		this.databaseService.findChat(id, chatId);
		
		/*
		 * Crea una nuova chiamata nel database nella chat
		 */
		this.databaseService.createCall(id, chatId);
		
		return ResponseEntity.created(null).build();
	}
	
	/**
	 * Ottiene le varie call che sono successe nella chat
	 * @param request
	 * @param response
	 * @param chatId
	 * @return
	 */
	@GetMapping("chats/{chat}/calls")
	public ResponseEntity<?> getCallsChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Ottiene le chiamate di una chat
		 */
		List<Call> calls = this.databaseService.getCalls(id, chatId);
		
		List<ResponseCallChatDTO> call = new ArrayList<ResponseCallChatDTO>();
		for(Call c : calls) call.add(new ResponseCallChatDTO(c.getPkID(), c.getStartTime(), c.getEndTime()));
		
		ResponseCallsChatDTO c = new ResponseCallsChatDTO(chatId, call);
		
		return ResponseEntity.ok().body(c);
	}
	
	/**
	 * Modifica l'endtime della call che e' attiva (al massimo 1 attiva)
	 * @param request
	 * @param response
	 * @param chatId
	 * @return
	 */
	@PutMapping("chats/callEnd")
	public ResponseEntity<?> patchCallChat(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticate(request, response);
		
		/*
		 * Ottiene le chiamate di una chat
		 */
		this.databaseService.updateCall(id);
		
		return ResponseEntity.ok().build();
	}
}
