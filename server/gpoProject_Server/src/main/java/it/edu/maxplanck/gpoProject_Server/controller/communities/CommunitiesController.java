package it.edu.maxplanck.gpoProject_Server.controller.communities;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.*;
import it.edu.maxplanck.gpoProject_Server.dto.response.*;
import org.springframework.http.HttpStatus;
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

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSubscriptionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Rest Controller che contiene gli endpoint per i servizi delle communities
 */
@RestController
@RequestMapping("api/services/communities")
public class CommunitiesController extends BasicApiRestController {

	public CommunitiesController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}

    @PostMapping("")
	public ResponseEntity<?> postCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body) {


		this.authenticationService.getAuthenticationRequestDTOService().authCommunityDTO(body);

		int userId = authenticate(request, response);

        List<Community> communities = this.databaseService.getRegistrationsRepo().findCommunitiesOfUser(userId);

        if (communities.size() > 10) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("message", "Hai troppe community registrate"));
        }

		Community c = this.databaseService.createCommunity(userId, body.isInviteCodeValid(), body.name(), body.description());
	    Section s = this.databaseService.createSection(userId, c.getPkID(), "generale");
        Channel ch = this.databaseService.createChannelCommunity(userId, s.getPkID(), "generale", "testo", "canale bello bello");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ch.getPkID())
                .toUri();

		return ResponseEntity.created(location).build();
	}

	@PostMapping("/subscribe")
	public ResponseEntity<?> postSubscriptionCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestSubscriptionDTO body) {

		int userId = authenticate(request, response);

        Registration r = this.databaseService.createRegistration(userId, body.inviteCode());
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/unsubscribe")
	public ResponseEntity<?> deleteSubscriptionCommunity(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer communityId) {

		int userId = authenticate(request, response);

		this.databaseService.deleteRegistration(userId, communityId);
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("")
	public ResponseEntity<?> getCommunities(HttpServletRequest request, HttpServletResponse response){
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene le community a cui un utente e' iscritto
		 */
		List<Community> communities = this.databaseService.findCommunitiesOfUser(id);
		if(communities == null || communities.isEmpty()) return ResponseEntity.ok().body(new ResponseCommunitiesDTO(new ArrayList<>()));
		
		List<ResponseCommunityDTO> c = new ArrayList<ResponseCommunityDTO>();
		for(Community com : communities) {
			
			List<Section> sections = this.databaseService.findSectionsOfCommunity(com);
			List<ResponseSectionDTO> sect = new ArrayList<ResponseSectionDTO>();
			
			if(sections != null) {
				for(Section sec : sections) {
					List<Channel> channels = this.databaseService.findChannelsOfSection(sec);
					List<ResponseChannelDTO> chan = new ArrayList<ResponseChannelDTO>();
					
					if(channels != null) {
						for(Channel ch : channels) {
							chan.add(new ResponseChannelDTO(ch.getPkID(), ch.getName(), ch.getType(), ch.getDescription(), ch.getCreatedAt()));
						}
					}
					
					sect.add(new ResponseSectionDTO(sec.getPkID(), sec.getName(), (chan.isEmpty())? null : chan));
				}
			}
			
			c.add(new ResponseCommunityDTO(com.getPkID(), com.getName(), com.getInviteCode(), com.isInviteCodeValid(), com.getDescription(), com.getCreatedAt(), (sect.isEmpty()? null : sect)));
		}
		
		ResponseCommunitiesDTO comm = new ResponseCommunitiesDTO(c);
		
		return ResponseEntity.ok().body(comm);
	}

}
