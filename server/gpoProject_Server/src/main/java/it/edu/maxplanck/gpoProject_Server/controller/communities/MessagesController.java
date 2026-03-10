package it.edu.maxplanck.gpoProject_Server.controller.communities;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.Channel;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageCommunity;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessagesChannelDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/services/communities/{communityId}/sections/{sectionId}/channels/{channelId}/messages")
public class MessagesController extends BasicApiRestController {
    public MessagesController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    public boolean verifyRequestParams(Integer userId, Community community, Section section, Channel channel) {

        if(!databaseService.isUserPartOfCommunity(userId, community)){
            return false;
        }

        if(!databaseService.isSectionPartOfCommunity(section.getPkID(), community)){
            return false;
        }

        if(!databaseService.isChannelPartOfSection(channel.getPkID(), section)){
            return false;
        }

        return true;
    }


    @GetMapping("")
    public ResponseEntity<?> getMessagesChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @PathVariable("sectionId") Integer sectionId, @PathVariable("channelId") Integer channelId, @RequestParam(value = "message", required = false, defaultValue = "0") Integer messageId) {

        int userId = this.authenticationService.authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().findById(communityId).orElseThrow();
        Section section = this.databaseService.getSectionsRepo().findById(sectionId).orElseThrow();
        Channel channel = this.databaseService.getChannelsRepo().findById(channelId).orElseThrow();

        if(!verifyRequestParams(userId, community, section, channel)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        List<MessageCommunity> messagesChannel = this.databaseService.getMessagesChannelSectionCommunity(userId, channelId, messageId);

        if(messagesChannel == null || messagesChannel.isEmpty())
            return ResponseEntity.ok().body(new ResponseMessagesChannelDTO(channelId, new ArrayList<>()));

        List<ResponseMessageChatDTO> messages = new ArrayList<ResponseMessageChatDTO>();

        for(MessageCommunity mess : messagesChannel){
            messages.add(new ResponseMessageChatDTO(mess.getPkID(), mess.getFkUser().getUsername(), mess.getMessage(), mess.getSentAt(), new ArrayList<>()));
        }

        ResponseMessagesChannelDTO m = new ResponseMessagesChannelDTO(channelId, messages);

        return ResponseEntity.ok().body(m);
    }

    @PostMapping("")
    public ResponseEntity<?> postMessageCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @PathVariable("sectionId") Integer sectionId, @PathVariable("channelId") Integer channelId, @RequestBody RequestMessageCommunityDTO body) {

        this.authenticationService.getAuthenticationRequestDTOService().authMessageCommunityDTO(body);

        int userId = this.authenticationService.authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().findById(communityId).orElseThrow();
        Section section = this.databaseService.getSectionsRepo().findById(sectionId).orElseThrow();
        Channel channel = this.databaseService.getChannelsRepo().findById(channelId).orElseThrow();

        if(!verifyRequestParams(userId, community, section, channel)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        MessageCommunity m = this.databaseService.createMessageChannel(userId, channelId, body.message());
        ResponseMessageChatDTO mes = new ResponseMessageChatDTO(m.getPkID(), m.getFkUser().getUsername(), m.getMessage(), m.getSentAt(), new ArrayList<>());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(m.getPkID())
                .toUri();

        return ResponseEntity.created(location).body(mes);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessage(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId,  @PathVariable("sectionId") Integer sectionId,  @PathVariable("channelId") Integer channelId,  @PathVariable("messageId") Integer messageId) {

        int userId = this.authenticationService.authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().findById(communityId).orElseThrow();
        Section section = this.databaseService.getSectionsRepo().findById(sectionId).orElseThrow();
        Channel channel = this.databaseService.getChannelsRepo().findById(channelId).orElseThrow();
        MessageCommunity messageCommunity = this.databaseService.getMessagesCommunityRepo().findById(messageId).orElseThrow();

        if(!verifyRequestParams(userId, community, section, channel)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        if(!messageCommunity.getFkChannel().getPkID().equals(channel.getPkID())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        ResponseMessageCommunityDTO responseMessageCommunityDTO = new ResponseMessageCommunityDTO(messageCommunity.getPkID(), messageCommunity.getFkUser().getUsername(), messageCommunity.getMessage(), messageCommunity.getSentAt());

        return ResponseEntity.ok(responseMessageCommunityDTO);
    }


}