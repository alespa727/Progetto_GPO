package it.edu.maxplanck.gpoProject_Server.controller.communities;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.Channel;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSectionDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseSectionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/services/communities/{communityId}/sections")
public class SectionController  extends BasicApiRestController {

    public SectionController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    /**
     * Crea una nuova sezione nella community
     */
    @PostMapping("")
    public ResponseEntity<?> postSection(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @RequestBody RequestSectionDTO body) {

        this.authenticationService.getAuthenticationRequestDTOService().authSectionDTO(body);

        int userId = authenticate(request, response);

        Section newSection = this.databaseService.createSection(userId, communityId, body.name());
        this.databaseService.createChannelCommunity(userId, newSection.getPkID(), "Default", "testo", "Descrizione di default");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSection.getPkID())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<?> deleteSection(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId, @PathVariable("sectionId") Integer sectionId) {

        /*
         * Autentificazione
         */
        int id = authenticate(request, response);

        /*
         * Elimina una community
         */
        this.databaseService.deleteSectionCommunity(id, sectionId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<?> getSection(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId,  @PathVariable("sectionId") Integer sectionId) {

        int userId = authenticate(request, response);

        Community c = this.databaseService.getCommunitiesRepo().getReferenceById(communityId);
        if(this.databaseService.isUserPartOfCommunity(userId, c)) {
            Section section = this.databaseService.getSectionsRepo().getReferenceById(sectionId);
            List<Channel> channelList = this.databaseService.getChannelsRepo().findChannelsByFkSection(section);
            List<ResponseChannelDTO> channelDTOList = new ArrayList<>();
            for (Channel channel : channelList) {
                channelDTOList.add(new ResponseChannelDTO(channel.getPkID(), channel.getName(), channel.getType(), c.getDescription(), c.getCreatedAt()));
            }
            return ResponseEntity.ok().body(new ResponseSectionDTO(section.getPkID(), section.getName(), channelDTOList));
        }

        return ResponseEntity.badRequest().build();
    }

}
