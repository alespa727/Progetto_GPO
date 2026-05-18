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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.AttachedChat;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAttachedChatDTO;
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
