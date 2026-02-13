package it.edu.maxplanck.gpoProject_Server.util;

/**
 * Classe di utilita' per il Database
 */
public final class UtilDatabase {

	public static int maxMessagesRead = 1000;
	
	// Model Attached -----------------------------------------------------
	public static class AttachedData{
		
		public static final String tableName = "attachments";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String  columnNameFkMessage = "fkMessage";
		
		
		public static final String columnNamePath = "path";
		public static final int pathLenght = 20;
		
		public static final String columnNameFilename = "filename";
		public static final int filenameLenght = 255;
		
		public static final String columnNameExtension = "extension";
		public static final int extensionLenght = 10;
	}
	
	// Model Call -----------------------------------------------------
	public static class CallData{
		
		public static final String tableName = "calls";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkChat = "fkChat";
		
		
		public static final String columnNameStartTime = "startTime";
		
		public static final String columnNameEndTime = "endTime";
	}

	// Model Channel -----------------------------------------------------
	public static class ChannelData{
		
		public static final String tableName = "channels";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkSection = "fkSection";
		
		
		public static final String columnNameName = "name";
		public static final int nameLenght = 100;
		
		public static final String columnNameType = "type";
		public static enum TypeType {
			
			TYPE_VOCALE("vocale"),
			TYPE_TESTO("testo");
			
			private final String type;
			private TypeType(String type) {
				this.type = type;
			}
			
			public String getType() {
				return type;
			}
		}
		
		public static final String columnNameDescription = "description";
		
		public static final String columnNameCreatedAt = "createdAt";
	}

	// Model Chat -----------------------------------------------------
	public static class ChatData{
		
		public static final String tableName = "chats";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkFriendship = "fkFriendship";
		
		
		public static final String columnNameTimeLastMessage = "timeLastMessage";
		
		public static final String columnNamecreatedAt = "createdAt";
		
		
	}

	// Model Community -----------------------------------------------------
	public static class CommunityData{
		
		public static final String tableName = "communities";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkUserOwner = "fkUserOwner";
		
		
		public static final String columnNameInviteCode = "inviteCode";
		public static final int inviteCodeLenght = 20;
		
		public static final String columnNameIsInviteCodeValid = "isInviteCodeValid";
		
		public static final String columnNameName = "name";
		public static final int nameLenght = 100;
		
		public static final String columnNameDescription = "description";
		
		public static final String columnNameCreatedAt = "createdAt";
	}

	// Model Friendship -----------------------------------------------------
	public static class FriendshipData{
		
		public static final String tableName = "friendships";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkUser1 = "fkUser1";
		public static final String columnNameFkUser2 = "fkUser2";
		
		
		public static final String columnNameCreatedAt = "createdAt";
	}

	// Model MessageChat -----------------------------------------------------
	public static class MessageChatData{
		
		public static final String tableName = "messagesChat";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkChat = "fkChat";
		public static final String columnNameFkUser = "fkUser";
		
		public static final String columnNameMessage = "message";
		public static final int messageLength = 2147483647;
		
		public static final String columnNameSentAt = "sentAt";
	}

	// Model MessageCommunity -----------------------------------------------------
	public static class MessageCommunityData{
		
		public static final String tableName = "messagesCommunity";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkChannel = "fkChannel";
		public static final String columnNameFkUser = "fkUser";
		
		
		public static final String columnNameMessage = "message";
		public static final int messageLength = 2147483647;
		
		public static final String columnNameSentAt = "sentAt";
	}

	// Model Registration -----------------------------------------------------
	public static class RegistrationData{
		
		public static final String tableName = "registrations";
		
		public static final String columnNameFkCommunity = "fkCommunity";
		public static final String columnNameFkUser = "fkUser";
		
		
		public static final String columnNameDate = "date";
	}

	// Model Section -----------------------------------------------------
	public static class SectionData{
		
		public static final String tableName = "sections";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameFkCommunity = "fkCommunity";
		
		
		public static final String columnNameName = "name";
		public static final int nameLenght = 30;
	}
	
	// Model User -----------------------------------------------------
	public static class UserData{
		
		public static final String tableName = "users";
		public static final String  columnNamePrimaryKey = "id";
		
		public static final String columnNameUsername = "username";
		public static final int usernameLength = 20;
		
		public static final String columnNamePassword = "password";
		public static final int passwordLength = 256;
		
		public static final String columnNameIsAdmin = "isAdmin";
		
		public static final String columnNameTimeLastAccess = "timeLastAccess";
		
		public static final String columnNameCreatedAt = "createdAt";
		
		public static final String columnNameImagePath = "imagePath";
		public static final int imagePathLenght = 100;
	}
}
