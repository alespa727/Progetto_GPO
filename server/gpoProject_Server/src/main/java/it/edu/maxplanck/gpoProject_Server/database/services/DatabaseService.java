package it.edu.maxplanck.gpoProject_Server.database.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.edu.maxplanck.gpoProject_Server.database.model.Call;
import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;
import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.repositories.*;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseExceptions;
import it.edu.maxplanck.gpoProject_Server.util.GenericUtil;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase;

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

	public void createUser(String username, String password) throws DatabaseException  {
		// TODO Auto-generated method stub
		
		if(username == null || password == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		if(this.usersRepo.existsUserByUsername(username)) throw new DatabaseException(DatabaseExceptions.DB_USERNAME_IS_ALREADY_IN_USE);
		
		password = GenericUtil.passwordEncoder.encode(password);
		while(GenericUtil.passwordEncoder.upgradeEncoding(password)) password = GenericUtil.passwordEncoder.encode(password);
		
		User u = new User(username, password);
		this.usersRepo.save(u);
	}

	public int findUser(String username, String password) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(username == null || password == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		User u = this.usersRepo.findUserByUsername(username);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		if(!u.getUsername().equals(username) || !GenericUtil.passwordEncoder.matches(password, u.getPassword())) throw new DatabaseException(DatabaseExceptions.DB_CREDENTIALS_ARE_INCORRECT);
		
		return u.getPkID();
	}
	
	public User findUser(String username) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(username == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		User u = this.getUsersRepo().findUserByUsername(username);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		return u;
	}
	
	public User findUser(Integer id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		if(id == null) throw new DatabaseException(DatabaseExceptions.DB_DATA_INSERTED_IS_NOT_VALID);
		
		User u = this.getUsersRepo().findById(id).orElse(null);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		return u;
	}

	public LocalDateTime updateStatusUser(int id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.usersRepo.findById(id).orElse(null);
		if(u == null) throw new DatabaseException(DatabaseExceptions.DB_USER_NOT_FOUND);
		
		if(u.getTimeLastAccess() == null) u.setTimeLastAccess(LocalDateTime.now());
		else u.setTimeLastAccess(null);
		
		return u.getTimeLastAccess();
	}

	public void updateUserAccount(int id, String username, String password) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		if(username != null) {
			
			if(this.usersRepo.existsUserByUsername(username)) throw new DatabaseException(DatabaseExceptions.DB_USERNAME_IS_ALREADY_IN_USE);
			u.setUsername(username);
		}
		
		if(password != null) {
			password = GenericUtil.passwordEncoder.encode(password);
			while(GenericUtil.passwordEncoder.upgradeEncoding(password)) password = GenericUtil.passwordEncoder.encode(password);
			u.setPassword(password);
		}
	}

	public void updateUserProfile(int id, String imagePath) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		
		if(imagePath != null) u.setImagePath(imagePath);
	}
	
	public void createFriendship(int id, String username) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u1 = this.findUser(id);
		User u2 = this.findUser(username);

		if(this.friendshipsRepo.existsByFkUser1AndFkUser2(u1, u2)) throw new DatabaseException(DatabaseExceptions.DB_FRIENDSHIP_ALREADY_CREATED);
		Friendship f = new Friendship(u1, u2);

		this.friendshipsRepo.save(f);
	}
	
	public List<User> findFriendsOfUser(int id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		List<User> friends = null;
		friends = this.friendshipsRepo.findFriendsByUserId(id);
		
		return friends;
	}

	public void createChat(int id, String username) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		this.findUser(username);
		
		Friendship f = this.friendshipsRepo.findFriendByUser1IdUser2Username(id, username);
		
		if(this.chatsRepo.existsByFkFriendship(f)) throw new DatabaseException(DatabaseExceptions.DB_CHAT_ALREADY_CREATED);
		Chat c = new Chat(f);
		this.chatsRepo.save(c);
	}
	
	public void createCommunity(int id, boolean isInviteCodeValid, String name, String description) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		
		String inviteCode = "";
		do {
			inviteCode = GenericUtil.generateString(UtilDatabase.CommunityData.inviteCodeLenght, GenericUtil.CHARSET);
		}while(this.communitiesRepo.existsCommunityByInviteCode(inviteCode) && inviteCode.length() <= UtilDatabase.CommunityData.inviteCodeLenght);
		
		Community c = new Community(u, inviteCode, isInviteCodeValid, name, description);
		this.communitiesRepo.save(c);
	}

	public void checkChat(int id, Chat c) throws DatabaseException {
		if(c == null) throw new DatabaseException(DatabaseExceptions.DB_CHAT_NOT_FOUND);
		if(!(c.getFkFriendship().getFkUser1().getPkID() == id) && !(c.getFkFriendship().getFkUser2().getPkID() == id)) throw new DatabaseException(DatabaseExceptions.DB_USER_IS_NOT_PART_OF_CHAT);
	}
	
	public void createMessageChat(int id, Integer chatId, String message) throws DatabaseException {
		// TODO Auto-generated method stub
		
		User u = this.findUser(id);
		Chat c = this.chatsRepo.findById(chatId).orElse(null);
		this.checkChat(id, c);
				
		MessageChat mChat = new MessageChat(c, u, message);
		this.messagesChatRepo.save(mChat);
	}

	public List<Chat> findChatsOfUser(int id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		List<Chat> chats = null;
		chats = this.chatsRepo.findChatByUserId(id);
		
		return chats;
	}

	public Chat findChat(int id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		Chat c = this.chatsRepo.findById(id).orElse(null);
		this.checkChat(id, c);
		
		return c;
	}

	public void deleteChat(int id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		Chat c = this.chatsRepo.findById(id).orElse(null);
		this.checkChat(id, c);
		
		this.chatsRepo.deleteById(c.getPkID());
	}

	public List<MessageChat> getMessagesChat(int id, Integer chatId, Integer messageId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findChat(id, chatId);
		if(messageId == null) messageId = 0;
		
		List<MessageChat> messages = this.messagesChatRepo.findByFkChatIDAndIDGreaterThanOrderByPkIDAsc(chatId, messageId, UtilDatabase.maxMessagesRead);
		
		return messages;
	}

	public void createCall(int id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		Chat chat = this.findChat(id, chatId);
		if(this.getCallsRepo().hasCallsOpen(id)) throw new DatabaseException(DatabaseExceptions.DB_CALL_STILL_OPEN);
		
		Call c = new Call(chat);
		this.callsRepo.save(c);
	}

	public List<Call> getCalls(int id, Integer chatId) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findChat(id, chatId);
		
		List<Call> calls = this.callsRepo.getCallsByFkChat(chatId);
		return calls;
	}

	public void updateCall(int id) throws DatabaseException {
		// TODO Auto-generated method stub
		
		this.findUser(id);
		
		Call c = this.callsRepo.getCallOfUser(id);
		if(c != null) {
			c.setEndTime(LocalDateTime.now());
			System.out.println("END TIME" + c.getEndTime());
		}
	}
}
