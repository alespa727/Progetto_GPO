package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.ArrayList;
import java.util.List;

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
import it.edu.maxplanck.gpoProject_Server.database.model.Channel;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageCommunity;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSectionDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSubscriptionDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAccountsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCommunitiesDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCommunityDataDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageChannelSectionCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessagesChannelSectionCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseSectionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rest Controller che contiene gli endpoint per i servizi delle communities
 */
@RestController
@RequestMapping("api/services")
public class ServiceApiCommunitiesController extends BasicApiRestController {

	public ServiceApiCommunitiesController(DatabaseService databaseService, AuthenticationService authenticationService) {
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
	public ResponseEntity<?> patchCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authCommunityDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Crea una nuova community in database
		 */
		this.databaseService.createCommunity(id, body.isInviteCodeValid(), body.name(), body.description());
	
		return ResponseEntity.created(null).build();
	}

	@PatchMapping("communities/{community}")
	public ResponseEntity<?> postCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body, @PathVariable("community") Integer communityId) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authCommunityDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Crea una nuova community in database
		 */
		this.databaseService.updateCommunity(id, communityId, body.isInviteCodeValid(), body.name(), body.description());
	
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("communities/{community}/subscription")
	public ResponseEntity<?> postSubscriptionCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @RequestBody RequestSubscriptionDTO body) {
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Fa la iscrizione alla community
		 */
		this.databaseService.createRegistration(id, communityId, body.inviteCode());
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("communities/{community}/deleteSubscription")
	public ResponseEntity<?> deleteSubscriptionCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId) {
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Fa la iscrizione alla community
		 */
		this.databaseService.deleteRegistration(id, communityId);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene i dati di una community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @return
	 */
	@GetMapping("communities/{community}/data")
	public ResponseEntity<?> getDataCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene i dati di una community
		 */
		Community c = this.databaseService.findCommunity(id, communityId);
		
		ResponseCommunityDataDTO community = new ResponseCommunityDataDTO(c.getName(), c.getInviteCode(), c.isInviteCodeValid(), c.getDescription(), c.getCreatedAt());
		
		return ResponseEntity.ok().body(community);
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
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene gli user della community
		 */
		List<User> u = this.databaseService.getUsersCommunity(id, commmunityId);
		if(u == null || u.isEmpty()) return ResponseEntity.ok().body(new ResponseAccountsDTO(null));
		
		List<ResponseAccountDTO> users = new ArrayList<ResponseAccountDTO>();
		for(User us : u) {
			String image = this.findImage(us);
			users.add(new ResponseAccountDTO(us.getUsername(), us.isAdmin(), us.getCreatedAt(), image));
		}
		
		ResponseAccountsDTO us = new ResponseAccountsDTO(users);
		
		return ResponseEntity.ok().body(us);
	}
	
	/**
	 * Ottiene tutte le community di quali faccio parte
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("communities")
	public ResponseEntity<?> getCommunities(HttpServletRequest request, HttpServletResponse response){
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene le community a cui un utente e' iscritto
		 */
		List<Community> communities = this.databaseService.findCommunitiesOfUser(id);
		if(communities == null) ResponseEntity.ok().body(new ResponseCommunitiesDTO(null));
		
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
			
			c.add(new ResponseCommunityDTO(com.getName(), com.getInviteCode(), com.isInviteCodeValid(), com.getDescription(), com.getCreatedAt(), (sect.isEmpty()? null : sect)));
		}
		
		ResponseCommunitiesDTO comm = new ResponseCommunitiesDTO(c);
		
		return ResponseEntity.ok().body(comm);
	}

	/**
	 * Elimina la community/l'iscrizione alla community
	 * @param request
	 * @param response
	 * @param commmunityId
	 * @return
	 */
	@DeleteMapping("communities/{community}")
	public ResponseEntity<?> deleteCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Elimina una community
		 */
		this.databaseService.deleteCommunity(id, communityId);
		
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
	public ResponseEntity<?> postSection(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @RequestBody RequestSectionDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authSectionDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Crea una nuova sezione in una community in database
		 */
		this.databaseService.createSection(id, communityId, body.name());
		
		return ResponseEntity.created(null).build();
	}

	@DeleteMapping("communities/{community}/sections/{section}")
	public ResponseEntity<?> deleteSection(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @PathVariable("section") Integer sectionId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Elimina una community
		 */
		this.databaseService.deleteSectionCommunity(id, communityId, sectionId);
		
		return ResponseEntity.ok().build();
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
	public ResponseEntity<?> postChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @PathVariable("section") Integer sectionId, @RequestBody RequestChannelDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authChannelDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Crea un nuovo canale in una sezione di una community in database
		 */
		this.databaseService.createChannelSectionCommunity(id, communityId, sectionId, body.name(), body.type(), body.description());
		
		return ResponseEntity.created(null).build();
	}

	@DeleteMapping("communities/{community}/sections/{section}/channels/{channel}")
	public ResponseEntity<?> deleteChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @PathVariable("section") Integer sectionId, @PathVariable("channel") Integer channelId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Elimina una community
		 */
		this.databaseService.deleteChannelSectionCommunity(id, communityId, sectionId, channelId);
		
		return ResponseEntity.ok().build();
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
	public ResponseEntity<?> postMessageCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @PathVariable("section") Integer sectionId, @PathVariable("channel") Integer channelId, @RequestBody RequestMessageCommunityDTO body) {

		/*
		 * Controlla se body request valido:
		 * 		- No -> Errore
		 */
		this.authenticationService.getAuthenticationRequestDTOService().authMessageCommunityDTO(body);
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Crea un messaggio in una determinata community
		 */
		this.databaseService.createMessageChannelSectionCommunity(id, communityId, sectionId, channelId, body.message());
		
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
	// GET /communities/{community}/sections/{section}/channels/{channel}/messages?message=123
	@GetMapping("communities/{community}/sections/{section}/channels/{channel}/messages")
	public ResponseEntity<?> getMessagesChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("community") Integer communityId, @PathVariable("section") Integer sectionId, @PathVariable("channel") Integer channelId, @RequestParam(value = "message", required = false, defaultValue = "0") Integer messageId) {

		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		/*
		 * Ottiene tutti i messaggi della community
		 */
		List<MessageCommunity> messagesChannel = this.databaseService.getMessagesChannelSectionCommunity(id, communityId, sectionId, channelId, messageId);
		if(messagesChannel == null) return ResponseEntity.ok().body(new ResponseMessagesChannelSectionCommunityDTO(null));
		
		List<ResponseMessageChannelSectionCommunityDTO> messages = new ArrayList<ResponseMessageChannelSectionCommunityDTO>();
		
		for(MessageCommunity mess : messagesChannel) messages.add(new ResponseMessageChannelSectionCommunityDTO(mess.getPkID(), mess.getMessage()));
		
		ResponseMessagesChannelSectionCommunityDTO m = new ResponseMessagesChannelSectionCommunityDTO((messages.isEmpty())? null : messages);
		
		return ResponseEntity.ok().body(m);
	}
}
