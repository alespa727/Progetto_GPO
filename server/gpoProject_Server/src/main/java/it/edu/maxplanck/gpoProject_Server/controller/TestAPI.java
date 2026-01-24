package it.edu.maxplanck.gpoProject_Server.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;

@RestController
@RequestMapping("api/test")
public class TestAPI extends BasicApiRestController {

	@Value("${app.upload.dir}")
	private String uploadDir;
	
	public TestAPI(DatabaseService databaseService, AuthenticationService authenticationService) {
		super(databaseService, authenticationService);
		// TODO Auto-generated constructor stub
	}
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("File vuoto");
	    }

	    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	    Path uploadPath = Paths.get(uploadDir);

	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }

	    Path filePath = uploadPath.resolve(fileName);
	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	    String imageUrl = "http://localhost:8080/images/" + fileName;
	    return ResponseEntity.ok(imageUrl);
	}
}
