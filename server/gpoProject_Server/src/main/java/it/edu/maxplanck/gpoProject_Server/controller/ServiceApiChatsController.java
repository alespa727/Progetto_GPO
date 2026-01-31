package it.edu.maxplanck.gpoProject_Server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.Attached;
import it.edu.maxplanck.gpoProject_Server.database.model.Call;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAttachedChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCallChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCallsChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChatsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessagesChatDTO;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataExceptions;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rest Controller che contiene gli endpoint per i servizi delle chat
 */
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
		int id = this.authenticationService.authenticate(request, response);
		
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
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene le varie chat dell'utente
		 */
		List<Chat> listChats = this.databaseService.findChatsOfUser(id);
		if(listChats == null || listChats.isEmpty()) return ResponseEntity.ok().body(new ResponseChatsDTO(null));
		
		List<ResponseChatDTO> c = new ArrayList<ResponseChatDTO>();
		for(Chat ch : listChats) {
			User u = (ch.getFkFriendship().getFkUser1().getPkID() == id)? ch.getFkFriendship().getFkUser2() : ch.getFkFriendship().getFkUser1();
			
			String image = this.findImage(u);
			c.add(new ResponseChatDTO(ch.getPkID(), new ResponseFriendDTO(u.getUsername(), image)));
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
		int id = this.authenticationService.authenticate(request, response);
		
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
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Crea un messaggio in una determinata chat
		 */
		this.databaseService.createMessageChat(id, chatId, body.message());
		
		return ResponseEntity.created(null).build();
	}
	
	/**
	 * Permette di inserire file fino ad un massimo settato oltre ad inviare il messaggio
	 * @param request
	 * @param response
	 * @param files
	 * @param description
	 * @return
	 */
	@PostMapping(value = "chats/{chat}/attachment")
	public ResponseEntity<?> postAttachmentChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId, @RequestParam("files") MultipartFile[] files, @RequestParam(value = "message", required = false) String message){
		
		ObjectMapper objectMapper = new ObjectMapper();
		RequestMessageChatDTO body = null;

		if (message != null && !message.isBlank()) {
		    try {
				body = objectMapper.readValue(message, RequestMessageChatDTO.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity.badRequest().build();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity.badRequest().build();
			}
		    
		    this.authenticationService.getAuthenticationRequestDTOService().authMessageChatDTO(body);
		}
		
		if(files.length == 0) return ResponseEntity.badRequest().body("Nessun file inviato");
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		this.databaseService.findUser(id);
		
		List<String> filename = new ArrayList<String>();
		List<String> extension = new ArrayList<String>();
		
		/*
		 * Permette tutti i file
		 */
		Tika tika = new Tika();
		MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();
		
		/*
		 * Salva il file nel server
		 */
		for (int i = 0; i < files.length; i++) {
			if(!files[i].isEmpty()) {
	            String fileName = null;
	            String extenc = null;
	    	    Path uploadPath = Paths.get(this.uploadDirFiles);

	    	    try {
	    	        GenericUtil.createDirectory(uploadPath);
	    	        
	    	        do {
	    		    	fileName = GenericUtil.generateString(((int)UtilDatabase.AttachedData.pathLenght / 4), GenericUtil.CHARSET);
	    		    }while(Files.exists(uploadPath.resolve(fileName)));
	    	        
	    	        String mimeType = tika.detect(files[i].getInputStream());
	    	        
	                if (GenericUtil.DENIED_MIME_TYPES.contains(mimeType)) {
	                	throw new DataException(DataExceptions.DATA_FILES_NOT_VALID);
	                }

	                MimeType tikaMime;
					try {
						tikaMime = mimeTypes.forName(mimeType);
					} catch (MimeTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new DataException(DataExceptions.DATA_FILES_NOT_VALID);
					}
					
					extenc = tikaMime.getExtension();
	    	        
	    	        GenericUtil.saveFile(uploadPath, files[i].getBytes(), fileName + extenc);
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	        fileName = null;
	    	    }
	    	    
	    	    if(fileName != null) {
	    	    	filename.add(fileName);
	    	    	extension.add(extenc);
	    	    }
			}
        }
		
		message = (body == null || body.message() == null)? null : body.message();
		this.databaseService.createAttachmentChat(id, chatId, filename, extension, message);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene un tot di messaggi(con allegati) della chat dal messaggio iniziale
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
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene i messaggi di una chat
		 */

		List<MessageChat> messages = this.databaseService.getMessagesChat(id, chatId, messageId);
		if(messages == null || messages.isEmpty()) return ResponseEntity.ok().body(new ResponseCallsChatDTO(chatId, null));
		
		List<ResponseMessageChatDTO> mess = new ArrayList<ResponseMessageChatDTO>();
		for(MessageChat m : messages) {
			List<Attached> attachedMessage = this.databaseService.getAttachmentsRepo().findByFkMessage(m.getPkID());
			List<ResponseAttachedChatDTO> attachements = new ArrayList<ResponseAttachedChatDTO>();
			
			for(Attached a : attachedMessage) {
				String path = this.findAttachment(a);
				if(path != null) attachements.add(new ResponseAttachedChatDTO(a.getPkID(), this.standardServerPath + this.standardPathFiles, a.getFilename(), a.getExtension()));
			}
			
			mess.add(new ResponseMessageChatDTO(m.getPkID(), m.getFkUser().getUsername(), m.getMessage(), m.getSentAt(), (attachements.isEmpty()? null : attachements)));
		}
		
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
		int id = this.authenticationService.authenticate(request, response);
		
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
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene le chiamate di una chat
		 */
		List<Call> calls = this.databaseService.getCalls(id, chatId);
		if(calls == null || calls.isEmpty()) return ResponseEntity.ok().body(new ResponseCallsChatDTO(chatId, null));
		
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
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene le chiamate di una chat
		 */
		this.databaseService.updateCall(id);
		
		return ResponseEntity.ok().build();
	}
}
