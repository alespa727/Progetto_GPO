package it.edu.maxplanck.gpoProject_Server.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.Attached;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;

/**
 * Classe astratta che ogni rest controller dovra' ereditare.
 * <br>Questa classe e' la base di tutte le API
 */
@RequestMapping("api")
public abstract class BasicApiRestController {
	
	@Value("${app.upload.dir.images}")
	protected String uploadDirImages;
	
	@Value("${app.upload.dir.files}")
	protected String uploadDirFiles;
	
	protected final DatabaseService databaseService;
	protected final AuthenticationService authenticationService;
	
	public BasicApiRestController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super();
		this.databaseService = databaseService;
		this.authenticationService = authenticationService;
	}
	
	/**
	 * Controlla se esiste una immagine profilo dell'utente
	 * @param u
	 * @return
	 */
	protected String findImage(User u) {
		String image = null;
		if(u.getImagePath() != null) {
			
			// Controllo se la immagine esiste o e' stata eliminata/persa
			Path imagePath = Paths.get(this.uploadDirImages).resolve(u.getImagePath());
			
			if (!Files.exists(imagePath)) {
				// immagine persa → reset DB
				this.databaseService.updateUserProfile(u.getPkID(), null);
			}else {
				image = GenericUtil.standardPathImages + u.getImagePath();
			}
		}
		
		return image;
	}
	
	/**
	 * Controlla se esiste il file dell'allegato
	 * @param a
	 * @return
	 */
	protected String findAttachment(Attached a) {
		String path = null;
		if(a.getPath() != null) {
			
			// Controllo se la immagine esiste o e' stata eliminata/persa
			Path imagePath = Paths.get(this.uploadDirFiles).resolve(a.getPath());
			
			if (!Files.exists(imagePath)) {
				// immagine persa → reset DB
				this.databaseService.deleteAttached(a.getPkID());
			}else {
				path = GenericUtil.standardPathFiles + a.getPath();
			}
		}
		
		return path;
	}
}
