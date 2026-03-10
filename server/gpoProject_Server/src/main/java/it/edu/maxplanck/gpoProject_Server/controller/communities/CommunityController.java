package it.edu.maxplanck.gpoProject_Server.controller.communities;


import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAccountsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCommunityDataDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/services/communities/{communityId}")
public class CommunityController extends BasicApiRestController {

    public CommunityController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    @GetMapping("")
    public ResponseEntity<?> getCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId) {

        int userId = this.authenticationService.authenticate(request, response);

        Community c = this.databaseService.findCommunity(userId, communityId);
        ResponseCommunityDataDTO community = new ResponseCommunityDataDTO(c.getName(), c.getInviteCode(), c.isInviteCodeValid(), c.getDescription(), c.getCreatedAt());

        return ResponseEntity.ok().body(community);
    }

    @PatchMapping("")
    public ResponseEntity<?> patchCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body, @PathVariable("communityId") Integer communityId) {

        this.authenticationService.getAuthenticationRequestDTOService().authCommunityDTO(body);

        int userId = this.authenticationService.authenticate(request, response);

        this.databaseService.updateCommunity(userId, communityId, body.isInviteCodeValid(), body.name(), body.description());
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("")
    public ResponseEntity<?> deleteCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId) {

        int userId = this.authenticationService.authenticate(request, response);

        this.databaseService.deleteCommunity(userId, communityId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/users")
    public ResponseEntity<?> getUsersCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer commmunityId) {

        int userId = this.authenticationService.authenticate(request, response);

        List<User> u = this.databaseService.getUsersCommunity(userId, commmunityId);
        if(u == null || u.isEmpty()) return ResponseEntity.ok().body(new ResponseAccountsDTO(null));

        List<ResponseAccountDTO> users = new ArrayList<>();
        for(User us : u) {
            String image = this.findImage(us);
            users.add(new ResponseAccountDTO(us.getUsername(), us.isAdmin(), us.getCreatedAt(), image));
        }

        ResponseAccountsDTO us = new ResponseAccountsDTO(users);

        return ResponseEntity.ok().body(us);
    }


}
