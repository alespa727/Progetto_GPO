package it.edu.maxplanck.gpoProject_Server.database.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.edu.maxplanck.gpoProject_Server.database.model.Attached;
import it.edu.maxplanck.gpoProject_Server.database.model.Call;
import it.edu.maxplanck.gpoProject_Server.database.model.Channel;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageCommunity;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.repositories.*;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataExceptions;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseExceptions;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase;

/**
 * Classe che serve come servizio per compiere le azioni sui dati del database
 */
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

	/**
	 * Crea un nuovo utente nel database
	 * <br>Errore se i dati sono incorretti o un altro utente esiste gia' con le stesse credenziali
	 * @param username
	 * @param password
	 * @throws DatabaseException
	 */
	public void createUser(String username, String password) throws DatabaseException  {
		// TODO Auto-generated method stub
		
		if(username == null || password == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		if(this.usersRepo.existsUserByUsername(username)) throw new DatabaseException(DatabaseExceptions.DB_USERNAME_IS_ALREADY_IN_USE);
		
		password = GenericUtil.passwordEncoder.encode(password);
		while(GenericUtil.passwordEncoder.upgradeEncoding(password)) password = GenericUtil.passwordEncoder.encode(password);
		
		User u = new User(username, password);
		this.usersRepo.save(u);
	}

	/**
	 * Cerca un utente in base ai dati inseriti
	 * <br>Errore se i dati non sono validi, se esiste gia' un altro utente con le stesse credenziali o se le credenziali sono errate
	 * @param username
	 * @param password
	 * @return
	 * @throws DatabaseException
	 */
	public int findUser(String username, String password) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(username == null || password == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		User u = this.usersRepo.findUserByUsername(username);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		if(!u.getUsername().equals(username) || !GenericUtil.passwordEncoder.matches(password, u.getPassword())) throw new DatabaseException(DatabaseExceptions.DB_CREDENTIALS_ARE_INCORRECT);
		
		return u.getPkID();
	}
	
	/**
	 * Cerca un utente in base all'id
	 * <br>Errore se i dati inseriti non sono validi o se non viene trovato
	 * @param id
	 * @return
	 * @throws DatabaseException
	 */
	public User findUser(Integer id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(id == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		User u = this.getUsersRepo().findById(id).orElse(null);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		return u;
	}
	
	/**
	 * Cerca un utente in base al nome
	 * <br>Errore se i dati inseriti non sono validi o se non viene trovato
	 * @param username
	 * @return
	 * @throws DatabaseException
	 */
	public User findUser(String username) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(username == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		User u = this.getUsersRepo().findUserByUsername(username);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		return u;
	}

	/**
	 * Fa l'update dello status dell'utente
	 * @param id
	 * @throws DatabaseException
	 */
	public void updateStatusUser(User u, Boolean status) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(status) u.setTimeLastAccess(null);
		else u.setTimeLastAccess(LocalDateTime.now());
	}
	
	/**
	 * Fa l'update dei dati dell'account dell'utente
	 * <br>Errore se i dati inseriti non sono corretti
	 * @param id
	 * @param username
	 * @param password
	 * @throws DatabaseException
	 */
	public void updateUserAccount(Integer id, String username, String password) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		
		// Se lo username e' stato cambiato
		if(username != null && !u.getUsername().equals(username)) {	
			if(this.usersRepo.existsUserByUsername(username)) throw new DatabaseException(DatabaseExceptions.DB_USERNAME_IS_ALREADY_IN_USE);
			u.setUsername(username);
		}
		
		// Se la password e' stata cambiata
		if(password != null && !GenericUtil.passwordEncoder.matches(password, u.getPassword())) {
			password = GenericUtil.passwordEncoder.encode(password);
			while(GenericUtil.passwordEncoder.upgradeEncoding(password)) password = GenericUtil.passwordEncoder.encode(password);
			u.setPassword(password);
		}
	}

	/**
	 * Fa l'update dei dati del profilo dell'utente
	 * @param id
	 * @param imagePath
	 * @throws DatabaseException
	 */
	public void updateUserProfile(Integer id, String imagePath) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		
		u.setImagePath(imagePath);
	}
	
	/**
	 * Crea una amicizia tra utenti
	 * <br>Errore se esiste gia'
	 * @param id
	 * @param username
	 * @throws DatabaseException
	 */
	public void createFriendship(Integer id, String username) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u1 = this.findUser(id);
		User u2 = this.findUser(username);

		if(this.friendshipsRepo.existsByFkUser1AndFkUser2(u1, u2)) throw new DatabaseException(DatabaseExceptions.DB_FRIENDSHIP_ALREADY_CREATED);
		Friendship f = new Friendship(u1, u2);

		this.friendshipsRepo.save(f);
	}
	
	/**
	 * Cerca tutti gli utenti con cui lo user ha amicizie
	 * @param id
	 * @return
	 * @throws DatabaseException
	 */
	public List<User> findFriendsOfUser(Integer id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		List<User> friends = null;
		friends = this.friendshipsRepo.findFriendsByUserId(id);
		
		return friends;
	}

	/**
	 * Crea una chat tra 2 utenti che sono amici
	 * <br>Errore se la chat esiste gia'
	 * @param id
	 * @param username
	 * @throws DatabaseException
	 */
	public void createChat(Integer id, String username) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		User u2 = this.findUser(username);
		
		// Controlla che esista una amicizia
		Friendship f = this.friendshipsRepo.findFriendByUser1IdUser2Id(id, u2.getPkID());
		if(f == null) throw new DatabaseException(DatabaseExceptions.DB_FRIENDSHIP_NOT_CREATED);
		
		// Controlla che non esista gia'
		if(this.chatsRepo.existsByFkFriendship(f)) throw new DatabaseException(DatabaseExceptions.DB_CHAT_ALREADY_CREATED);
		
		Chat c = new Chat(f);
		this.chatsRepo.save(c);
	}
	
	/**
	 * Crea una community con owner l'utente
	 * @param id
	 * @param isInviteCodeValid
	 * @param name
	 * @param description
	 * @throws DatabaseException
	 */
	public void createCommunity(Integer id, boolean isInviteCodeValid, String name, String description) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		
		String inviteCode = null;
		do {
			inviteCode = GenericUtil.generateString(UtilDatabase.CommunityData.inviteCodeLenght, GenericUtil.CHARSET);
		}while(this.communitiesRepo.existsCommunityByInviteCode(inviteCode) && inviteCode.length() <= UtilDatabase.CommunityData.inviteCodeLenght);
		
		Community c = new Community(u, inviteCode, isInviteCodeValid, name, description);
		this.communitiesRepo.save(c);
	}

	/**
	 * Controlla se lo user fa parte della chat
	 * <br>Errore se non ne fa parte
	 * @param id
	 * @param c
	 * @throws DatabaseException
	 */
	public boolean isUserPartOfChat(Integer id, Chat c) throws DatabaseException {
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_CHAT_NOT_FOUND);
		return ((c.getFkFriendship().getFkUser1().getPkID() == id) || (c.getFkFriendship().getFkUser2().getPkID() == id));
	}
	
	/**
	 * Cerca la chat dell'utente
	 * <br> Errore se non esiste o se lo user non ne fa parte
	 * @param id
	 * @param chatId
	 * @return
	 * @throws DatabaseException
	 */
	public Chat findChat(Integer id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(chatId == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		this.findUser(id);
		
		Chat c = this.chatsRepo.findById(chatId).orElse(null);
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_CHAT_NOT_FOUND);
		if(!this.isUserPartOfChat(id, c)) throw new DatabaseException(DatabaseExceptions.DB_USER_IS_NOT_PART_OF_CHAT);
		
		return c;
	}
	
	/**
	 * Cerca tutte le chat in cui e' stato aggiunto l'utente
	 * @param id
	 * @return
	 * @throws DatabaseException
	 */
	public List<Chat> findChatsOfUser(Integer id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		List<Chat> chats = null;
		chats = this.chatsRepo.findChatByUserId(id);
		
		return chats;
	}
	
	/**
	 * Crea un messaggio in una chat
	 * @param id
	 * @param chatId
	 * @param message
	 * @throws DatabaseException
	 */
	public void createMessageChat(Integer id, Integer chatId, String message) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		Chat c = this.findChat(id, chatId);
		
		MessageChat mChat = new MessageChat(c, u, message);
		this.messagesChatRepo.save(mChat);
	}

	/**
	 * Elimina una chat
	 * @param id
	 * @param chatId
	 * @throws DatabaseException
	 */
	public void deleteChat(Integer id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		Chat c = this.findChat(id, chatId);
		
		this.chatsRepo.deleteById(c.getPkID());
	}

	/**
	 * Ottiene tutti i messaggi inviati di recente in una chat
	 * @param id
	 * @param chatId
	 * @param messageId
	 * @return
	 * @throws DatabaseException
	 */
	public List<MessageChat> getMessagesChat(Integer id, Integer chatId, Integer messageId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findChat(id, chatId);
		if(messageId == null) messageId = 0;
		
		List<MessageChat> messages = this.messagesChatRepo.findByFkChatIDAndIDGreaterThanOrderByPkIDAsc(chatId, messageId, UtilDatabase.maxMessagesRead);
		return messages;
	}

	/**
	 * Crea una chiamata nella chat
	 * <br>Errore se ha gia' un'altra chiamata ancora attiva
	 * @param id
	 * @param chatId
	 * @throws DatabaseException
	 */
	public void createCall(Integer id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		Chat chat = this.findChat(id, chatId);
		if(this.getCallsRepo().hasCallsOpen(id)) throw new DatabaseException(DatabaseExceptions.DB_CALL_STILL_OPEN);
		
		Call c = new Call(chat);
		this.callsRepo.save(c);
	}

	/**
	 * Ottiene tutte le chiamate che sono avvenute nella chat
	 * @param id
	 * @param chatId
	 * @return
	 * @throws DatabaseException
	 */
	public List<Call> getCalls(Integer id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findChat(id, chatId);
		
		List<Call> calls = this.callsRepo.getCallsByFkChat(chatId);
		return calls;
	}

	/**
	 * Aggiorna lo status della chiamata attiva
	 * @param id
	 * @throws DatabaseException
	 */
	public void updateCall(Integer id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		Call c = this.callsRepo.getCallOfUser(id);
		if(c != null) c.setEndTime(LocalDateTime.now());
	}

	/**
	 * Crea un allegato ad un messaggio nella chat
	 * @param id
	 * @param chatId
	 * @param pathFiles
	 * @param message
	 * @throws DatabaseException
	 */
	public void createAttachmentChat(int id, Integer chatId, List<String> filename, List<String> extension, String message) throws DatabaseException {
		// TODO Auto-generated method stub
		
		Chat c = this.findChat(id, chatId);
		MessageChat mc = new MessageChat(c, this.findUser(id), message);
		this.messagesChatRepo.save(mc);
		
		for(int i = 0; i < filename.size(); i++) {
			if(filename.get(i).length() <= UtilDatabase.AttachedData.filenameLenght && extension.get(i).length() <= UtilDatabase.AttachedData.extensionLenght) this.attachmentsRepo.save(new Attached(mc, filename.get(i), extension.get(i)));
		}
	}

	/**
	 * Cerca un allegato
	 * @param id
	 * @return
	 * @throws DatabaseException
	 */
	public Attached findAttached(Integer id) throws DatabaseException {
		if(id == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		Attached a = this.attachmentsRepo.findById(id).orElse(null);
		if(a == null) throw new DatabaseException(DatabaseExceptions.DB_ATTACHED_NOT_FOUND);
		
		return a;
	}
	
	/**
	 * Aggiorna i dati di un allegato
	 * @param pkID
	 * @param path
	 * @throws DatabaseException
	 */
	public void updateAttached(Integer pkID, String fileName, String extension) throws DatabaseException {
		// TODO Auto-generated method stub
		
		Attached a = this.findAttached(pkID);
		
		a.setFilename(fileName);
		a.setExtension(extension);
	}

	/**
	 * Elimina un allegato
	 * @param pkID
	 * @throws DatabaseException
	 */
	public void deleteAttached(Integer pkID) throws DatabaseException {
		// TODO Auto-generated method stub
		
		Attached a = this.findAttached(pkID);
		
		this.attachmentsRepo.delete(a);
	}

	public Community findCommunity(Integer id, Integer commmunityId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		Community c = this.communitiesRepo.findById(commmunityId).orElse(null);
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_COMMUNITY_NOT_FOUND);
		if(!this.isUserPartOfCommunity(id, c)) throw new DatabaseException(DatabaseExceptions.DB_USER_IS_NOT_PART_OF_COMMUNITY);
		
		return c;
	}

	public boolean isUserPartOfCommunity(Integer id, Community c) throws DatabaseException {
		// TODO Auto-generated method stub
		User u = this.findUser(id);
		
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_COMMUNITY_NOT_FOUND);
		
		return (c.getFkUserOwner().getPkID() == id || this.registrationsRepo.existsRegistrationByFkUserAndFkCommunity(u, c));
	}
	
	public List<User> getUsersCommunity(Integer id, Integer communityId) throws DatabaseException {
		
		User u = this.findUser(id);
		
		Community c = this.communitiesRepo.findById(communityId).orElse(null);
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_COMMUNITY_NOT_FOUND);
		
		List<User> usersCommunity = this.registrationsRepo.findUsersOfCommunty(communityId);
		if(usersCommunity == null) usersCommunity = new ArrayList<User>();
		usersCommunity.add(u);
		
		return usersCommunity;
	}

	public List<Community> findCommunitiesOfUser(Integer id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		
		List<Community> communities = this.registrationsRepo.findCommunitiesOfUser(id);
		List<Community> owner = this.communitiesRepo.findCommunityByFkUserOwner(u);
		
		if(communities == null && owner == null) return null;
		
		if(communities == null) return owner;
		if(owner == null) return communities;
		
		for(Community c : owner) communities.add(c);
		
		return communities;
	}

	public List<Section> findSectionsOfCommunity(Community community) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(community == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		List<Section> s = this.sectionsRepo.findSectionsByFkCommunity(community);
		
		return s;
	}

	public List<Channel> findChannelsOfSection(Section section) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(section == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		List<Channel> c = this.channelsRepo.findChannelsByFkSection(section);
		
		return c;
	}

	public void deleteCommunity(Integer id, Integer communityId) throws DatabaseException, DataException {
		// TODO Auto-generated method stub
		
		Community c = this.findCommunity(id, communityId);
		if(c.getFkUserOwner().getPkID() != id || this.findUser(id).isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		this.communitiesRepo.delete(c);
	}

	public void createSection(Integer id, Integer communityId, String name) throws DatabaseException, DataException {
		// TODO Auto-generated method stub
		
		Community c = this.findCommunity(id, communityId);
		if(c.getFkUserOwner().getPkID() != id || this.findUser(id).isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		Section s = new Section(c, name);
		
		this.sectionsRepo.save(s);
	}

	public void createChannelSectionCommunity(Integer id, Integer communityId, Integer sectionId, String name, String type, String description) throws DatabaseException, DataException {
		// TODO Auto-generated method stub
		
		Community c = this.findCommunity(id, communityId);
		if(c.getFkUserOwner().getPkID() != id || this.findUser(id).isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		Section s = this.findSection(sectionId);
		
		Channel ch = new Channel(s, name, type, description);
		
		this.channelsRepo.save(ch);
	}

	public Section findSection(Integer sectionId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(sectionId == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		Section s = this.sectionsRepo.findById(sectionId).orElse(null);
		if(s == null) throw new DatabaseException(DatabaseExceptions.DB_SECTION_NOT_FOUND);
		
		return s;
	}

	public void createMessageChannelSectionCommunity(Integer id, Integer communityId, Integer sectionId, Integer channelId, String message) throws DatabaseException, DataException {
		// TODO Auto-generated method stub
		
		this.findCommunity(id, communityId);
		this.findSection(sectionId);
		
		User u = this.findUser(id);
		Channel ch = this.findChannel(channelId);
		
		if(!ch.getType().equals("testo")) throw new DatabaseException(DatabaseExceptions.DB_CHANNEL_IS_NOT_FOR_TEXT);
		
		MessageCommunity m = new MessageCommunity(ch, u, message);
		
		this.messagesCommunityRepo.save(m);
	}

	public Channel findChannel(Integer channelId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(channelId == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		Channel c = this.channelsRepo.findById(channelId).orElse(null);
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_CHANNEL_NOT_FOUND);
		
		return c;
	}

	public List<MessageCommunity> getMessagesChannelSectionCommunity(Integer id, Integer communityId, Integer sectionId, Integer channelId, Integer messageId)  throws DatabaseException, DataException {
		// TODO Auto-generated method stub
		
		Community c = this.findCommunity(id, communityId);
		Section s = this.findSection(sectionId);
		Channel ch = this.findChannel(channelId);
		
		if(!this.isSectionPartOfCommunity(s.getPkID(), c)) throw new DatabaseException(DatabaseExceptions.DB_SECTION_IS_NOT_PART_OF_COMMUNITY);
		if(!this.isChannelPartOfSection(ch.getPkID(), s)) throw new DatabaseException(DatabaseExceptions.DB_CHANNEL_IS_NOT_PART_OF_SECTION);
		
		if(messageId == null) messageId = 0;
		
		List<MessageCommunity> messages = this.messagesCommunityRepo.findByFkChannelIDAndIDGreaterThanOrderByPkIDAsc(channelId, messageId, UtilDatabase.maxMessagesRead);
		
		return messages;
	}
	
	public boolean isSectionPartOfCommunity(Integer sectionId, Community community) throws DatabaseException {
		
		if(community == null || sectionId == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		return (this.sectionsRepo.existsSectionByPkIDAndFkCommunity(sectionId, community));
	}

	public boolean isChannelPartOfSection(Integer channelId, Section section) throws DatabaseException {
		
		if(section == null || channelId == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		return (this.channelsRepo.existsChannelByPkIDAndFkSection(channelId, section));
	}
	
	public void deleteSectionCommunity(int id, Integer communityId, Integer sectionId) {
		// TODO Auto-generated method stub
		
		Community c = this.findCommunity(id, communityId);
		if(c.getFkUserOwner().getPkID() != id || this.findUser(id).isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		Section s = this.findSection(sectionId);
		
		this.sectionsRepo.delete(s);
	}

	public void deleteChannelSectionCommunity(int id, Integer communityId, Integer sectionId, Integer channelId) {
		// TODO Auto-generated method stub
		
		Community c = this.findCommunity(id, communityId);
		if(c.getFkUserOwner().getPkID() != id || this.findUser(id).isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		this.findSection(sectionId);
		Channel ch = this.findChannel(channelId);
		
		this.channelsRepo.delete(ch);
	}
}
