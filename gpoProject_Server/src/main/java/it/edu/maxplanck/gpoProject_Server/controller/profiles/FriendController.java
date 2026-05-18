package it.edu.maxplanck.gpoProject_Server.controller.profiles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendsDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessage;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services/friends")
public class FriendController extends BasicApiRestController {

    public FriendController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    @GetMapping("")
    public ResponseEntity<?> getFriends(HttpServletRequest request, HttpServletResponse response) {
        int userId = this.authenticationService.authenticate(request, response);

        List<User> listFriends = this.databaseService.findFriendsOfUser(userId);
        List<ResponseFriendDTO> f = new ArrayList<>();
        for(User u : listFriends) {
            String image = this.findImage(u);

            if(image == null) image = "";

            image = image.replace("http://localhost:8080", "");

            f.add(new ResponseFriendDTO(
                    u.getUsername(),
                    u.getDescription(),
                    image
            ));
        }

        ResponseFriendsDTO friends = new ResponseFriendsDTO(f);
        return ResponseEntity.ok().body(friends);
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getFriendRequests(HttpServletRequest request, HttpServletResponse response) {
        int userId = this.authenticationService.authenticate(request, response);

        List<User> listFriends = this.databaseService.findFriendRequestsOfUser(userId);
        List<ResponseFriendDTO> f = new ArrayList<>();
        for(User u : listFriends) {
            String image = this.findImage(u);

            if(image == null) image = "";

            image = image.replace("http://localhost:8080", "");

            f.add(new ResponseFriendDTO(
                    u.getUsername(),
                    u.getDescription(),
                    image
            ));
        }

        ResponseFriendsDTO friends = new ResponseFriendsDTO(f);
        return ResponseEntity.ok().body(friends);
    }

    @PostMapping("")
    public ResponseEntity<?> postFriend(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestFriendDTO body) {
        try {
            this.authenticationService.getAuthenticationRequestDTOService().authFriendDTO(body);
            int id = this.authenticationService.authenticate(request, response);


            User user = this.databaseService.findUser(id);

            if(user.getUsername().equals(body.username())) return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Non puoi inviare la richiesta a te stesso"));

            this.databaseService.createFriendship(id, body.username());
            this.databaseService.createChat(id, body.username());

            return ResponseEntity.ok().body((new ResponseMessage("Amicizia creata con successo")));

        } catch (DatabaseException e) {

            return ResponseEntity
                    .status(e.getExceptions().getResponseStatus())
                    .body(new ResponseMessage(e.getExceptions().getMessage()));
        }
    }
}
