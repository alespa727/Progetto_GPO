package it.edu.maxplanck.gpoProject_Server.database.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.repositories.*;

@Transactional
@Service
public class DatabaseService {

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

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public DatabaseService(
			AttachmentsRepo attachmentsRepo, 
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

	public BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void createUser(String username, String password) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		if(username == null || password == null) throw new IllegalArgumentException("Dati non validi");
		
		if(this.usersRepo.existsUserByUsername(username)) throw new IllegalArgumentException("Username gia' in uso");
		
		password = this.passwordEncoder.encode(password);
		while(this.passwordEncoder.upgradeEncoding(password)) password = this.passwordEncoder.encode(password);
		
		User u = new User(username, password);
		this.usersRepo.save(u);
	}

	public int findUser(String username, String password) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		if(username == null || password == null) throw new IllegalArgumentException("Dati non validi");
		
		User u = this.usersRepo.findUserByUsername(username);
		if(u == null) throw new IllegalArgumentException("Utente non trovato");
		
		if(!u.getUsername().equals(username) || !this.passwordEncoder.matches(password, u.getPassword())) throw new IllegalArgumentException("Credenziali errate");
		
		return u.getPkID();
	}

	public void updateUser(int id, LocalDateTime time) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		User u = this.usersRepo.findById(id).orElse(null);
		if(u == null) throw new IllegalArgumentException("Utente non trovato");
		
		u.setTimeLastAccess(time);
	}

	// ------------------------------------------------------------------------------------
}
