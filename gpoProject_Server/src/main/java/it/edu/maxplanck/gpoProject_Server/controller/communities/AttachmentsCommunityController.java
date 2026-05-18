package it.edu.maxplanck.gpoProject_Server.controller.communities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.controller.BasicApiRestController;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataExceptions;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services/communities" +
        "/{communityId}/sections/{sectionId}/channels/{channelId}/attachments")
public class AttachmentsCommunityController extends BasicApiRestController {
    public AttachmentsCommunityController(DatabaseService databaseService, AuthenticationService authenticationService) {
        super(databaseService, authenticationService);
    }


    @PostMapping(value = "")
    public ResponseEntity<?> postAttachmentCommunity(HttpServletRequest request, HttpServletResponse response, @PathVariable("communityId") Integer communityId,  @PathVariable("sectionId") Integer sectionId,  @PathVariable("channelId") Integer channelId, @RequestParam("files") MultipartFile[] files, @RequestParam(value = "message", required = false, defaultValue = "") String message){

        if(files.length == 0) return ResponseEntity.badRequest().body("Nessun file inviato");

        int userId = this.authenticationService.authenticate(request, response);
        this.databaseService.findUser(userId);


        List<String> originalNames = new ArrayList<>();
        List<String> filename = new ArrayList<>();
        List<String> extension = new ArrayList<>();

        /*
         * Permette tutti i file
         */
        Tika tika = new Tika();
        MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();

        /*
         * Salva il file nel server
         */
        for (MultipartFile file : files) {

            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                originalName = originalName.substring(0, originalName.lastIndexOf("."));
            }
            System.out.println(originalName);

            String fileName = null;
            String extenc = null;
            Path uploadPath = Paths.get(this.uploadDirFiles);

            try {
                GenericUtil.createDirectory(uploadPath);

                originalNames.add(originalName);
                do {
                    fileName = GenericUtil.generateString(((int) UtilDatabase.AttachedData.pathLenght / 4), GenericUtil.CHARSET);
                } while (Files.exists(uploadPath.resolve(fileName)));

                String mimeType = tika.detect(file.getInputStream());

                MimeType tikaMime;
                try {
                    tikaMime = mimeTypes.forName(mimeType);
                } catch (MimeTypeException e) {
                    // e.printStackTrace();
                    throw new DataException(DataExceptions.DATA_FILES_NOT_VALID);
                }

                extenc = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));

                GenericUtil.saveFile(uploadPath, file.getBytes(), fileName + extenc);

            } catch (IOException e) {
                // e.printStackTrace();
                fileName = null;
            }

            if (fileName != null) {
                filename.add(fileName);
                extension.add(extenc);
            }
        }

        ResponseMessageChatDTO r = this.databaseService.createAttachmentCommunity(userId, communityId, sectionId, channelId, originalNames, filename, extension, message);

        return ResponseEntity.created(null).body(r);
    }

}
