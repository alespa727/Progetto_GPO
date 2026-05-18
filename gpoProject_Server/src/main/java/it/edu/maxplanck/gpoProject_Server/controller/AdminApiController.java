package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.Channel;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseSectionDTO;
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
		
		this.authenticationService.authenticate(request, response);

		List<ResponseFriendDTO> usersImagePaths = new ArrayList<>();
		for(RequestFriendDTO r : body.users()) {
			User utente = null;
			try{
				utente = this.databaseService.findUser(r.username());
			} catch(Exception e) {
				// e.printStackTrace();
			}
            assert utente != null;
            usersImagePaths.add(new ResponseFriendDTO(utente.getUsername(), utente.getDescription(), this.findImage(utente)));
		}

		ResponseFriendsDTO users = new ResponseFriendsDTO(usersImagePaths);

		return ResponseEntity.ok().body(users);
	}
    @PostMapping("chat")
    public ResponseEntity<?> chat(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer chatId){

        int id = this.authenticationService.authenticate(request, response);

        Chat c = this.databaseService.findChat(id, chatId);

        return ResponseEntity.ok().body(c.getFkFriendship());
    }
    @PostMapping("server")
    public ResponseEntity<ResponseCommunityDTO> server(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer serverId) {

        int id = this.authenticationService.authenticate(request, response);

        Community com = this.databaseService.findCommunity(id, serverId);

        if (com == null) {
            return ResponseEntity.notFound().build();
        }

        List<Section> sections = this.databaseService.findSectionsOfCommunity(com);
        List<ResponseSectionDTO> sect = new ArrayList<>();

        if (sections != null) {
            for (Section sec : sections) {

                List<Channel> channels = this.databaseService.findChannelsOfSection(sec);
                List<ResponseChannelDTO> chan = new ArrayList<>();

                if (channels != null) {
                    for (Channel ch : channels) {
                        chan.add(new ResponseChannelDTO(
                                ch.getPkID(),
                                ch.getName(),
                                ch.getType(),
                                ch.getDescription(),
                                ch.getCreatedAt()
                        ));
                    }
                }

                sect.add(new ResponseSectionDTO(
                        sec.getPkID(),
                        sec.getName(),
                        chan.isEmpty() ? null : chan
                ));
            }
        }

        ResponseCommunityDTO dto = new ResponseCommunityDTO(
                com.getPkID(),
                com.getName(),
                com.getInviteCode(),
                com.getFkUserOwner().getUsername(),
                com.isInviteCodeValid(),
                com.getDescription(),
                com.getCreatedAt(),
                sect.isEmpty() ? null : sect
        );

        return ResponseEntity.ok(dto);
    }
}
