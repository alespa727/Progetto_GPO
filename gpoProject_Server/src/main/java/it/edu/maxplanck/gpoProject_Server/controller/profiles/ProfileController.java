package it.edu.maxplanck.gpoProject_Server.controller.profiles;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.DeleteAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.PatchProfileDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCredentials;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessage;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseProfileDTO;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import it.edu.maxplanck.gpoProject_Server.exceptions.TokenException;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("api/services/profile")
public class ProfileController extends BasicApiRestController {
    public ProfileController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }

    @GetMapping("")
    public ResponseEntity<?> getAccount(HttpServletRequest request, HttpServletResponse response) {

        int id;
        try {
            id = this.authenticationService.authenticate(request, response);
        } catch (TokenException e) {
            // Token scaduti o invalidi → pulizia cookie e risposta 401
            response.addCookie(this.authenticationService.getCookieService()
                    .generateCookie(UtilServer.accessCookieName, "", true, false, "/api/", 0));
            response.addCookie(this.authenticationService.getCookieService()
                    .generateCookie(UtilServer.refreshCookieName, "", true, false, "/api/", 0));

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Sessione scaduta, effettua nuovamente il login"));
        }

        User u = this.databaseService.findUser(id);

        String image = (u.getImagePath() == null)
                ? null
                : this.standardServerPath + this.standardPathImages + u.getImagePath();

        ResponseAccountDTO responseDTO = new ResponseAccountDTO(
                u.getUsername(), u.getDescription(), u.isAdmin(), u.getCreatedAt(), image);

        return ResponseEntity.ok().body(responseDTO);
    }
    @PatchMapping("")
    public ResponseEntity<?> patchAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody PatchProfileDTO body) {

        int id = this.authenticationService.authenticate(request, response);
        this.databaseService.updateUserAccount(id, body.username(), body.description());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request, HttpServletResponse response, DeleteAccountDTO body) {

        int userId;
        try {
            userId = this.authenticationService.authenticate(request, response);
            User user = this.databaseService.findUser(userId);

            this.authenticationService.getAuthenticationRequestDTOService().deleteDTO(body);
            this.databaseService.findUser(user.getUsername(), body.password());

            this.databaseService.getUsersRepo().deleteById(userId);
        }catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Password errata"));
        }

        response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.accessCookieName, "", true, false, "/api/", 0));
        response.addCookie(this.authenticationService.getCookieService().generateCookie(UtilServer.refreshCookieName, "", true, false, "/api/", 0));

        return ResponseEntity.ok().body(new ResponseMessage("Profilo eliminato"));
    }

    @GetMapping("/pfp")
    public ResponseEntity<?> getPfp(HttpServletRequest request, HttpServletResponse response) {

        int userId = this.authenticationService.authenticate(request, response);

        User u = this.databaseService.findUser(userId);
        String image = this.findImage(u);
        ResponseProfileDTO r = new ResponseProfileDTO(image);

        return ResponseEntity.ok().body(r);
    }

    @PatchMapping("/pfp")
    public ResponseEntity<?> patchPfp(HttpServletRequest request, HttpServletResponse response, @RequestParam("image") MultipartFile file) {

        if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "File vuoto"));

        int id = this.authenticationService.authenticate(request, response);
        User u = this.databaseService.findUser(id);

        try {
            if (!GenericUtil.isFileValidImage(file)) return ResponseEntity.badRequest().body(Map.of("message", "File non immagine"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        byte[] imageBytes;
        try {
            imageBytes = GenericUtil.makeSquare(file.getInputStream());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String fileName = null;
        Path uploadPath = Paths.get(this.uploadDirImages);

        try {
            GenericUtil.createDirectory(uploadPath);

            // elimina vecchia immagine
            if (u.getImagePath() != null) GenericUtil.removeFile(uploadPath, u.getImagePath());

            do {
                fileName = GenericUtil.generateString(((int) UtilDatabase.UserData.imagePathLenght / 2), GenericUtil.CHARSET) + ".jpg";
            }while(Files.exists(uploadPath.resolve(fileName)));

            // Salva nuova immagine
            GenericUtil.saveFile(uploadPath, imageBytes, fileName);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        this.databaseService.updateUserProfile(id, fileName);

        return ResponseEntity.ok().body(new ResponseMessage("Immagine profilo cambiata"));
    }

    @DeleteMapping("/pfp")
    public ResponseEntity<?> deletePfp(HttpServletRequest request, HttpServletResponse response) {

        int userId = this.authenticationService.authenticate(request, response);

        User u = this.databaseService.findUser(userId);

        Path uploadPath = Paths.get(this.uploadDirImages);

        try {
            GenericUtil.createDirectory(uploadPath);

            if (u.getImagePath() != null) GenericUtil.removeFile(uploadPath, u.getImagePath());

            this.databaseService.updateUserProfile(userId, "");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().body(new ResponseMessage("Immagine profilo eliminata"));
    }



}
