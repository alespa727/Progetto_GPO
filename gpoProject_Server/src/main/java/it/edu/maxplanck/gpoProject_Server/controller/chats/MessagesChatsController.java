package it.edu.maxplanck.gpoProject_Server.controller.chats;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.AttachedChat;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAttachedChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageChatDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/services/chats/{chatId}/messages")
public class MessagesChatsController extends BasicApiRestController {
    public MessagesChatsController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    @Transactional
    @PostMapping("")
    public ResponseEntity<?> postMessageChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chatId") Integer chatId, @RequestBody RequestMessageChatDTO body) {

        this.authenticationService.getAuthenticationRequestDTOService().authMessageChatDTO(body);

        int userId = this.authenticationService.authenticate(request, response);

        Chat chat = this.databaseService.findChat(userId, chatId);
        MessageChat c = this.databaseService.createMessageChat(userId, chatId, body.message());
        if(c!=null){
            chat.setTimeLastMessage( java.time.LocalDateTime.now());

        }

        ResponseMessageChatDTO res = new ResponseMessageChatDTO(c.getId(), c.getFkUser().getUsername(), c.getMessage(), c.getSentAt(), new ArrayList<>());

        return ResponseEntity.created(null).body(res);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessageChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chatId") Integer chatId, @PathVariable("messageId") Integer messageId) {


        int userId = this.authenticationService.authenticate(request, response);
        this.databaseService.deleteMessageChat(userId, chatId, messageId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<?> patchMessageChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chatId") Integer chatId, @PathVariable("messageId") Integer messageId, @RequestBody RequestMessageChatDTO body) {


        int userId = this.authenticationService.authenticate(request, response);
        this.databaseService.modifyMessageChat(userId, chatId, messageId, body.message());
        return ResponseEntity.noContent().build();
    }



    @GetMapping("")
    public ResponseEntity<?> getMessagesChat(HttpServletRequest request, HttpServletResponse response, @PathVariable("chatId") Integer chatId, @RequestParam(value = "message", required = false, defaultValue = "0") Integer messageId) {

        int id = this.authenticationService.authenticate(request, response);

        List<MessageChat> messages = this.databaseService.getMessagesChat(id, chatId, messageId);
        if(messages==null) messages = new ArrayList<>();
        List<ResponseMessageChatDTO> mess = new ArrayList<ResponseMessageChatDTO>();
        for(MessageChat m : messages) {
            List<AttachedChat> attachedChatMessage = this.databaseService.getAttachmentsRepo().findByFkMessage(m.getId());
            List<ResponseAttachedChatDTO> attachements = new ArrayList<ResponseAttachedChatDTO>();

            for(AttachedChat a : attachedChatMessage) {
                String path = this.findAttachment(a);
                if(path != null) attachements.add(new ResponseAttachedChatDTO(a.getId(), this.standardServerPath + this.standardPathFiles,  a.getOriginalname(), a.getFilename(), a.getExtension()));
            }

            mess.add(new ResponseMessageChatDTO(m.getId(), m.getFkUser().getUsername(), m.getMessage(), m.getSentAt(), attachements));
        }

        return ResponseEntity.ok().body(mess);
    }




}
