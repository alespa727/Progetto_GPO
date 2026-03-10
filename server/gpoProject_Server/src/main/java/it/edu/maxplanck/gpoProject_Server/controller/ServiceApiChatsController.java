package it.edu.maxplanck.gpoProject_Server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.http.HttpStatus;
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
	 * Ottiene tutte le chat create
	 * @param request
	 * @param response
	 * @return
	 */

	
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
		MessageChat c = this.databaseService.createMessageChat(id, chatId, body.message());
		ResponseMessageChatDTO res = new ResponseMessageChatDTO(c.getPkID(), c.getFkUser().getUsername(), c.getMessage(), c.getSentAt(), new ArrayList<>());

		return ResponseEntity.created(null).body(res);
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


	                MimeType tikaMime;
					try {
						tikaMime = mimeTypes.forName(mimeType);
					} catch (MimeTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new DataException(DataExceptions.DATA_FILES_NOT_VALID);
					}

					extenc = tikaMime.getExtension();

	    	        GenericUtil.saveFile(uploadPath, files[i].getBytes(), fileName + extension);
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

		
		return ResponseEntity.created(null).build();
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
	    if(messages==null) messages = new ArrayList<>();
		List<ResponseMessageChatDTO> mess = new ArrayList<ResponseMessageChatDTO>();
		for(MessageChat m : messages) {
			List<Attached> attachedMessage = this.databaseService.getAttachmentsRepo().findByFkMessage(m.getPkID());
			List<ResponseAttachedChatDTO> attachements = new ArrayList<ResponseAttachedChatDTO>();
			
			for(Attached a : attachedMessage) {
				String path = this.findAttachment(a);
				if(path != null) attachements.add(new ResponseAttachedChatDTO(a.getPkID(), this.standardServerPath + this.standardPathFiles, a.getFilename(), a.getExtension()));
			}
			
			mess.add(new ResponseMessageChatDTO(m.getPkID(), m.getFkUser().getUsername(), m.getMessage(), m.getSentAt(), attachements));
		}
		
		ResponseMessagesChatDTO m = new ResponseMessagesChatDTO(chatId, mess);
		
		return ResponseEntity.ok().body(m);
	}
	

    /*
	@GetMapping("chats/{chat}/calls")
	public ResponseEntity<?> getCallsChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chat") Integer chatId) {

		int id = this.authenticationService.authenticate(request, response);
		

		List<Call> calls = this.databaseService.getCalls(id, chatId);
		if(calls == null || calls.isEmpty()) return ResponseEntity.ok().body(new ResponseCallsChatDTO(chatId, null));
		
		List<ResponseCallChatDTO> call = new ArrayList<ResponseCallChatDTO>();
		for(Call c : calls) call.add(new ResponseCallChatDTO(c.getPkID(), c.getStartTime(), c.getEndTime()));
		
		ResponseCallsChatDTO c = new ResponseCallsChatDTO(chatId, call);
		return ResponseEntity.ok().body(c);
	}*/

}
