package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
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
