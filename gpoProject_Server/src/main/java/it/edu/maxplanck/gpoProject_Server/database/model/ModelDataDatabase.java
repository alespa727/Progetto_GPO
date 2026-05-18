package it.edu.maxplanck.gpoProject_Server.database.model;

public class ModelDataDatabase {

	public static final class AttachedChatData {
		public static final String tableName = "attachmentsChat";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkMessage = "fkMessage";
		public static final String constraintNameFkMessage = "fkAttachedChatMessageChat";

		public static final String columnNamePath = "path";
		public static final int pathLenght = 20;

		public static final String columnNameFilename = "filename";
		public static final int filenameLenght = 255;

		public static final String columnNameExtension = "extension";
		public static final int extensionLenght = 10;
	}

	public static final class AttachedCommunityData {
		public static final String tableName = "attachmentsCommunity";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkMessage = "fkMessage";
		public static final String constraintNameFkMessage = "fkAttachedCommunityMessageCommunity";

		public static final String columnNamePath = "path";
		public static final int pathLenght = 20;

		public static final String columnNameFilename = "filename";
		public static final int filenameLenght = 255;

		public static final String columnNameExtension = "extension";
		public static final int extensionLenght = 10;
	}

	public static class CallChatData {

		public static final String tableName = "callsChat";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkChat = "fkChat";
		public static final String constraintNameFkChat = "fkCallChat";

		public static final String columnNameStartTime = "startTime";

		public static final String columnNameEndTime = "endTime";
	}

	public static class ChannelData {

		public static final String tableName = "channels";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";
		
		public static final String columnNameFkSection = "fkSection";
		public static final String constraintNameFkSection = "fkChannelSection";

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

	public static class ChatData {

		public static final String tableName = "chats";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkFriendship = "fkFriendship";
		public static final String constraintNameFkFriendship = "fkChatFriendship";

		public static final String columnNameTimeLastMessage = "timeLastMessage";

		public static final String columnNamecreatedAt = "createdAt";

	}

	public static class CommunityData {

		public static final String tableName = "communities";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkUserOwner = "fkUserOwner";
		public static final String constraintNameFkUser = "fkCommunityUser";

		public static final String columnNameInviteCode = "inviteCode";
		public static final int inviteCodeLenght = 20;

		public static final String columnNameIsInviteCodeValid = "isInviteCodeValid";

		public static final String columnNameName = "name";
		public static final int nameLenght = 100;

		public static final String columnNameDescription = "description";

		public static final String columnNameCreatedAt = "createdAt";
	}

	public static class FriendshipData {

		public static final String tableName = "friendships";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkUser1 = "fkUser1";
		public static final String constraintNameFkUser1 = "fkFriendshipUser1";
		
		public static final String columnNameFkUser2 = "fkUser2";
		public static final String constraintNameFkUser2 = "fkFriendshipUser2";

		public static final String columnNameCreatedAt = "createdAt";
	}

	public static class MessageChatData {

		public static final String tableName = "messagesChat";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkChat = "fkChat";
		public static final String constraintNameFkChat = "fkMessageChatChat";
		
		public static final String columnNameFkUser = "fkUser";
		public static final String constraintNameFkUser = "fkMessageChatUser";

		public static final String columnNameMessage = "message";
		public static final int messageLength = 2147483647;

		public static final String columnNameSentAt = "sentAt";
	}

	public static class MessageCommunityData {

		public static final String tableName = "messagesCommunity";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkChannel = "fkChannel";
		public static final String constraintNameFkChannel = "fkMessageCommunityChannel";
		
		public static final String columnNameFkUser = "fkUser";
		public static final String constraintNameFkUser = "fkMessageCommunityUser";
		
		public static final String columnNameMessage = "message";
		public static final int messageLength = 2147483647;

		public static final String columnNameSentAt = "sentAt";
	}

	public static class RegistrationData {

		public static final String tableName = "registrations";

		public static final String columnNameFkCommunity = "fkCommunity";
		public static final String constraintNameFkCommunity = "fkRegistrationCommunity";
		
		public static final String columnNameFkUser = "fkUser";
		public static final String constraintNameFkUser = "RegistrationUser";
		
		public static final String columnNameDate = "date";
	}

	public static class SectionData {

		public static final String tableName = "sections";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameFkCommunity = "fkCommunity";
		public static final String constraintNameFkCommunity = "fkSectionCommunity";
		
		public static final String columnNameName = "name";
		public static final int nameLenght = 30;
	}

	public static class UserData {

		public static final String tableName = "users";
		public static final String columnNamePrimaryKey = "id";
		public static final String typePK = "int unsigned zerofill";

		public static final String columnNameUsername = "username";
		public static final int usernameLength = 20;


        public static final String columnNameDescription = "description";
        public static final int descriptionLength = 150;

		public static final String columnNamePassword = "password";
		public static final int passwordLength = 256;

		public static final String columnNameIsAdmin = "isAdmin";

		public static final String columnNameTimeLastAccess = "timeLastAccess";

		public static final String columnNameCreatedAt = "createdAt";

		public static final String columnNameImagePath = "imagePath";
		public static final int imagePathLenght = 100;
	}
}
