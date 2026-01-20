package it.edu.maxplanck.gpoProject_Server.database.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.repositories.AttachmentsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.CallsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.ChannelsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.ChatsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.CommunitiesRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.FriendshipsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.MessagesChatRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.MessagesCommunityRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.RegistrationsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.SectionsRepo;
import it.edu.maxplanck.gpoProject_Server.database.repositories.UsersRepo;

@Transactional
@Service
public class DatabaseService {

	/**
	 * -------------------------------------------------------------------------
	 *  			ATTRIBUTI
	 * -------------------------------------------------------------------------
	**/
	private final AttachmentsRepo attachmentsRepo;
	private final CallsRepo callsRepo;
	private final ChannelsRepo channelsRepo;
	private final ChatsRepo chatsRepo;
	private final CommunitiesRepo communitiesRepo;
	private final FriendshipsRepo friendshipsRepo;
	private final MessagesChatRepo messagesChatRepo;
	private final MessagesCommunityRepo messagesCommunityRepo;
	private final RegistrationsRepo registrationsRepo;
	private final SectionsRepo sectionsRepo;
	private final UsersRepo usersRepo;
	
	/**
	 * -------------------------------------------------------------------------
	 *  			COSTRUTTORI
	 * -------------------------------------------------------------------------
	**/
	public DatabaseService(AttachmentsRepo attachmentsRepo, 
			CallsRepo callsRepo, 
			ChannelsRepo channelsRepo,
			ChatsRepo chatsRepo, 
			CommunitiesRepo communitiesRepo, 
			FriendshipsRepo friendshipsRepo,
			MessagesChatRepo messagesChatRepo, 
			MessagesCommunityRepo messagesCommunityRepo,
			RegistrationsRepo registrationsRepo, 
			SectionsRepo sectionsRepo, 
			UsersRepo usersRepo
		) {
		super();
		this.attachmentsRepo = attachmentsRepo;
		this.callsRepo = callsRepo;
		this.channelsRepo = channelsRepo;
		this.chatsRepo = chatsRepo;
		this.communitiesRepo = communitiesRepo;
		this.friendshipsRepo = friendshipsRepo;
		this.messagesChatRepo = messagesChatRepo;
		this.messagesCommunityRepo = messagesCommunityRepo;
		this.registrationsRepo = registrationsRepo;
		this.sectionsRepo = sectionsRepo;
		this.usersRepo = usersRepo;
	}

	/**
	 * -------------------------------------------------------------------------
	 *  			GETTER
	 * -------------------------------------------------------------------------
	**/
	public AttachmentsRepo getAttachmentsRepo() {
		return attachmentsRepo;
	}

	public CallsRepo getCallsRepo() {
		return callsRepo;
	}

	public ChannelsRepo getChannelsRepo() {
		return channelsRepo;
	}

	public ChatsRepo getChatsRepo() {
		return chatsRepo;
	}

	public CommunitiesRepo getCommunitiesRepo() {
		return communitiesRepo;
	}

	public FriendshipsRepo getFriendshipsRepo() {
		return friendshipsRepo;
	}

	public MessagesChatRepo getMessagesChatRepo() {
		return messagesChatRepo;
	}

	public MessagesCommunityRepo getMessagesCommunityRepo() {
		return messagesCommunityRepo;
	}

	public RegistrationsRepo getRegistrationsRepo() {
		return registrationsRepo;
	}

	public SectionsRepo getSectionsRepo() {
		return sectionsRepo;
	}

	public UsersRepo getUsersRepo() {
		return usersRepo;
	}
	
	/**
	 * -------------------------------------------------------------------------
	 *  			METODI
	 * -------------------------------------------------------------------------
	**/
	
	/// Metodi FRIENDSHIP
	
	// Ottiene amici
	public List<Friendship> getFriends() {

		List<Friendship> friends = null;

		return friends;
	}
	
	/// Metodi USER
	
	// Crea un utente
	public void createUser(User user) throws IllegalArgumentException, NullPointerException {
		
		if(user == null) throw new NullPointerException("Errore valore null");
		
		if (this.usersRepo.existsByUsername(user.getUsername())) {
		    throw new IllegalArgumentException("Username already exists");
		}
		
		this.usersRepo.save(user);
	}
	
	// Ottiene un utente in base all'id
	public User getUser(Integer pk) throws NullPointerException {
		
		if(pk == null) throw new NullPointerException("Errore valore null");
		
		return this.usersRepo.findById(pk).get();
	}
	
	// Ottiene un utente in base allo username
	
	
	// Aggiorna i dati di un utente
	public void updateUser(User modifiedUser) throws NullPointerException, IllegalArgumentException {
		
		if(modifiedUser == null) throw new NullPointerException("Errore valore null");
		
		User u = this.usersRepo.findById(modifiedUser.getPkID()).orElse(null);
		
		if(u == null) throw new NullPointerException("Errore valore null");
		
		if(!u.getUsername().equals(modifiedUser.getUsername())) {
			
			if (this.usersRepo.existsByUsername(modifiedUser.getUsername())) {
			    throw new IllegalArgumentException("Username already exists");
			}
			
			u.setUsername(modifiedUser.getUsername());
		}
		
		if(!u.getPassword().equals(modifiedUser.getPassword())) {
			u.setPassword(modifiedUser.getPassword());
		}
		
		if(u.isAdmin() && !modifiedUser.isAdmin()) {
			u.setAdmin(modifiedUser.isAdmin());
		}
		
		if(!u.getTimeLastAccess().equals(modifiedUser.getTimeLastAccess())) {
			u.setTimeLastAccess(modifiedUser.getTimeLastAccess());
		}
		
		if(!u.getImagePath().equals(modifiedUser.getImagePath())) {
			u.setImagePath(modifiedUser.getImagePath());
		}
	}
	
	public void deleteUser(User user) throws NullPointerException {
		
		if(user == null) throw new NullPointerException("Errore valore null");
		
		this.usersRepo.delete(user);
	}
}
