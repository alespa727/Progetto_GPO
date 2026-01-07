package it.edu.maxplanck.gpoProject_Server.database.service;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.edu.maxplanck.gpoProject_Server.database.modelDB.Utente;
import it.edu.maxplanck.gpoProject_Server.database.repository.CommunityRepository;
import it.edu.maxplanck.gpoProject_Server.database.repository.UtenteRepository;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestUserDTO;
import it.edu.maxplanck.gpoProject_Server.errorMessages.TypeErrorMessages;
import it.edu.maxplanck.gpoProject_Server.exceptions.CustomException;
import it.edu.maxplanck.gpoProject_Server.util.CryptoUtil;

@Service
public class ServiceDatabase {

	private final CommunityRepository communityRepo;
	private final UtenteRepository userRepo;

	public ServiceDatabase(CommunityRepository communityRepo, UtenteRepository userRepo) {
		this.communityRepo = communityRepo;
		this.userRepo = userRepo;
	}

	@Transactional
	public Utente postUtente(RequestUserDTO user) {
		if (this.userRepo.existsById(user.username())) {
			throw new CustomException("" + TypeErrorMessages.USERNAME_ALREADY_EXIST, "Username esiste già",
					HttpStatus.CONFLICT, null);
		}

		Utente u = new Utente();
		u.setUsername(user.username());
		u.setPassword(CryptoUtil.hashPassword(user.password()));
		u.setIsAdmin(user.isAdmin());
		u.setCreatedAt(Instant.now());

		return userRepo.save(u); // SALVA nel DB
	}

	public Utente getUtente(RequestUserDTO user) {
		Utente u = this.userRepo.findById(user.username())
				.orElseThrow(() -> new CustomException("" + TypeErrorMessages.INEXISTENT_USER, "Utente inesistente",
						HttpStatus.BAD_REQUEST, null));

		if (!CryptoUtil.matches(user.password(), u.getPassword())) {
			throw new CustomException("" + TypeErrorMessages.WRONG_PASSWORD_INSERTED, "Password errata",
					HttpStatus.CONFLICT, null);
		}

		return u;
	}

	public CommunityRepository getCommunityRepo() {
		return communityRepo;
	}

	public UtenteRepository getUserRepo() {
		return userRepo;
	}
}
