package it.edu.maxplanck.gpoProject_Server.controller.chats;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriendDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/services/chats")
public class ChatsController extends BasicApiRestController {
    public ChatsController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    @PostMapping("")
    public ResponseEntity<?> postChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChatDTO body) {

        this.authenticationService.getAuthenticationRequestDTOService().authChatDTO(body);

        int userId = this.authenticationService.authenticate(request, response);

        this.databaseService.createChat(userId, body.friend().username());

        return ResponseEntity.created(null).build();
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChat(HttpServletRequest request, HttpServletResponse response, @RequestParam("chatId") Integer chatId) {

        int userId = this.authenticationService.authenticate(request, response);

        Chat chat =  this.databaseService.findChat(userId, chatId);
        User u = (chat.getFkFriendship().getFkUser1().getId() == userId)? chat.getFkFriendship().getFkUser2() : chat.getFkFriendship().getFkUser1();

        ResponseChatDTO dto = new ResponseChatDTO(chat.getId(), chat.getTimeLastMessage(), new ResponseFriendDTO(u.getUsername(), u.getDescription(), this.findImage(u)));
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("")
    public ResponseEntity<?> getChats(HttpServletRequest request, HttpServletResponse response) {

        int userId = this.authenticationService.authenticate(request, response);

        List<Chat> listChats = this.databaseService.findChatsOfUser(userId);
        if(listChats == null){
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<ResponseChatDTO> responseChatDTOS = new ArrayList<ResponseChatDTO>();
        for(Chat ch : listChats) {
            User u = (ch.getFkFriendship().getFkUser1().getId() == userId)? ch.getFkFriendship().getFkUser2() : ch.getFkFriendship().getFkUser1();

            String image = this.findImage(u);
            if(image==null) image = "default";
            responseChatDTOS.add(new ResponseChatDTO(ch.getId(), ch.getTimeLastMessage(), new ResponseFriendDTO(u.getUsername(), u.getDescription(), image)));
        }

        return ResponseEntity.ok().body(responseChatDTOS);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chatId") Integer chatId) {

        int userId = this.authenticationService.authenticate(request, response);

        this.databaseService.deleteChat(userId, chatId);

        return ResponseEntity.ok().build();
    }


}
