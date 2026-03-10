package it.edu.maxplanck.gpoProject_Server.controller.chats;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.Call;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCall;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseCallChatDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("api/services/calls")
public class CallController extends BasicApiRestController {
    public CallController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    @PostMapping("")
    public ResponseEntity<?> createCall(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCall requestCall) {
        try{
            Integer userId = authenticate(request, response);
            User server = this.databaseService.getUsersRepo().findById(userId).orElseThrow();

            if(!server.isAdmin()){
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Not Authorized"));
            }

            User caller = this.databaseService.getUsersRepo().findUserByUsername(requestCall.caller());
            User called = this.databaseService.getUsersRepo().findUserByUsername(requestCall.called());

            Friendship friendship = this.databaseService.getFriendshipsRepo().findFriendByUser1IdUser2Id(caller.getPkID(), called.getPkID());
            Chat chat = this.databaseService.getChatsRepo().findChatByFkFriendship(friendship);
            Call call = this.databaseService.createCall(userId, chat.getPkID());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("id", call.getPkID()));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }
    }

    @PatchMapping("/{callId}/close")
    public ResponseEntity<?> endCall(HttpServletRequest request, HttpServletResponse response, @RequestParam("callId") Integer callId) {
        try {
            Integer userId = authenticate(request, response);
            User server = this.databaseService.getUsersRepo().findById(userId).orElseThrow();

            if (!server.isAdmin()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Not Authorized"));
            }

            this.databaseService.endCall(callId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of("status", "closed"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }
    }

    @GetMapping("/{callId}")
    public ResponseEntity<?> getCallInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam("callId") Integer callId) {
        try{
            Integer userId = authenticate(request, response);
            User server = this.databaseService.getUsersRepo().findById(userId).orElseThrow();

            if(!server.isAdmin()){
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Not Authorized"));
            }

            Call call = this.databaseService.getCallsRepo().findById(callId).orElseThrow();
            ResponseCallChatDTO res = new ResponseCallChatDTO(call.getPkID(), call.getStartTime(), call.getEndTime());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(res);
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Parametri non validi"));
        }
    }
}
