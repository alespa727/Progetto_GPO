package it.edu.maxplanck.gpoProject_Server.controller.communities;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.Channel;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChannelDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services/communities/{communityId}/sections/{sectionId}/channels")
public class ChannelController extends BasicApiRestController {

    public ChannelController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    public boolean verifyRequestParams(Integer userId, Community community, Section section) {

        if(!databaseService.isUserPartOfCommunity(userId, community)){
            return false;
        }

        return databaseService.isSectionPartOfCommunity(section.getPkID(), community);
    }

    @PostMapping("")
    public ResponseEntity<?> postChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @PathVariable("sectionId") Integer sectionId, @RequestBody RequestChannelDTO body) {

        this.authenticationService.getAuthenticationRequestDTOService().authChannelDTO(body);

        int userId = authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().findById(communityId).orElse(null);
        Section section = this.databaseService.getSectionsRepo().findById(sectionId).orElse(null);

        if(!verifyRequestParams(userId, community, section)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        /*
         * Crea un nuovo canale in una sezione di una community in database
         */
        this.databaseService.createChannelCommunity(userId, sectionId, body.name(), body.type(), body.description());

        return ResponseEntity.created(null).build();
    }



    @DeleteMapping("/{channelId}")
    public ResponseEntity<?> deleteChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @PathVariable("sectionId") Integer sectionId, @PathVariable("channelId") Integer channelId) {

        /*
         * Autentificazione
         */
        int userId = authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().findById(communityId).orElseThrow();
        Section section = this.databaseService.getSectionsRepo().findById(sectionId).orElseThrow();

        if(!verifyRequestParams(userId, community, section)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        if(!databaseService.isChannelPartOfSection(channelId, section)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        this.databaseService.deleteChannelSectionCommunity(userId, channelId);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{channelId}")
    public ResponseEntity<?> patchChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @PathVariable("sectionId") Integer sectionId, @PathVariable("channelId") Integer channelId, @RequestBody RequestChannelDTO body) {

        /*
         * Autentificazione
         */
        int userId = authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().findById(communityId).orElseThrow();
        Section section = this.databaseService.getSectionsRepo().findById(sectionId).orElseThrow();

        if(!verifyRequestParams(userId, community, section)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        if(!databaseService.isChannelPartOfSection(channelId, section)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        Channel channel = this.databaseService.getChannelsRepo()
                .findById(channelId)
                .orElseThrow();

        channel.setName(body.name());
        channel.setType(body.type());
        channel.setDescription(body.description());

        return ResponseEntity.ok(channel);
    }




    @GetMapping("/{channelId}")
    public ResponseEntity<?> getChannel(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId,  @PathVariable("sectionId") Integer sectionId,  @PathVariable("channelId") Integer channelId) {

        int userId = this.authenticationService.authenticate(request, response);

        Community community = this.databaseService.getCommunitiesRepo().getReferenceById(communityId);
        Section section = this.databaseService.getSectionsRepo().getReferenceById(sectionId);

        if(!verifyRequestParams(userId, community, section)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }

        Channel channel = this.databaseService.getChannelsRepo()
                .findById(channelId)
                .orElseThrow();
        ResponseChannelDTO responseChannelDTO = new ResponseChannelDTO(channelId, channel.getName(), channel.getType(), channel.getDescription(), channel.getCreatedAt());

        return ResponseEntity.ok(responseChannelDTO);
    }




}
