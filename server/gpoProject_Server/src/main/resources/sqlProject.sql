
/* SQL */

DROP DATABASE progetto_gpo;
CREATE DATABASE progetto_gpo;
use progetto_gpo;


/* TABLES */

CREATE TABLE attachments (
	id INT AUTO_INCREMENT,
	fkMessage INT NOT NULL,
	path VARCHAR(255) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE calls (
	id INT AUTO_INCREMENT,
	fkChat INT NOT NULL,
	startTime DATETIME DEFAULT CURRENT_TIMESTAMP(),
	endTime DATETIME,
	PRIMARY KEY(id)
);

CREATE TABLE channels (
	id INT AUTO_INCREMENT,
	fkSection INT NOT NULL,
	name VARCHAR(100) NOT NULL,
	type ENUM('testo', 'vocale') NOT NULL,
	description TEXT,
	createdAt DATE DEFAULT CURDATE(),
	PRIMARY KEY(id)
);

CREATE TABLE chats (
	id INT AUTO_INCREMENT,
	fkFriendship INT NOT NULL,
	timeLastMessage DATETIME,
	createdAt DATE DEFAULT CURDATE(),
	PRIMARY KEY(id)
);
ALTER TABLE chats ADD CONSTRAINT uniqueChatFriendship unique(fkFriendship);

CREATE TABLE communities (
	id INT AUTO_INCREMENT,
	fkUserOwner INT NOT NULL,
	inviteCode VARCHAR(20) NOT NULL,
	isInviteCodeValid TINYINT(1) DEFAULT 0,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	createdAt DATE DEFAULT CURDATE(),
	PRIMARY KEY(id)
);
ALTER TABLE communities ADD CONSTRAINT uniqueInviteCode unique(inviteCode);
ALTER TABLE communities ADD CONSTRAINT checkIsInviteCodeValid check(isInviteCodeValid = 0 OR isInviteCodeValid = 1);

CREATE TABLE friendships (
	id INT AUTO_INCREMENT,
	fkUser1 INT NOT NULL,
	fkUser2 INT NOT NULL,
	createdAt DATE DEFAULT CURDATE(),
	PRIMARY KEY(id)
);
ADD CONSTRAINT uniqueFriendship unique(fkUser1, fkUser2);

CREATE TABLE messagesChat (
	id INT AUTO_INCREMENT,
	fkChat INT NOT NULL,
	fkUser INT NOT NULL,
	message TEXT,
	sentAt DATETIME DEFAULT CURRENT_TIMESTAMP(),
	PRIMARY KEY(id)
);

CREATE TABLE messagesCommunity (
	id INT AUTO_INCREMENT,
	fkChannel INT NOT NULL,
	fkUser INT NOT NULL,
	message TEXT,
	sentAt DATETIME DEFAULT CURRENT_TIMESTAMP(),
	PRIMARY KEY(id)
);

CREATE TABLE registrations (
	fkCommunity INT,
	fkUser INT,
	date DATETIME DEFAULT CURRENT_TIMESTAMP(),
	PRIMARY KEY(fkCommunity, fkUser)
);

CREATE TABLE sections (
	id INT AUTO_INCREMENT,
	fkCommunity INT NOT NULL,
	name VARCHAR(30) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE users (
	id INT AUTO_INCREMENT,
	username VARCHAR(20) NOT NULL,
	password VARCHAR(256) NOT NULL,
	isAdmin TINYINT(1) DEFAULT 0,
	timeLastAccess DATETIME DEFAULT CURRENT_TIMESTAMP(),
	createdAt DATE DEFAULT CURDATE(),
	imagePath VARCHAR(100),
	PRIMARY KEY(id)
);
ALTER TABLE users ADD CONSTRAINT checkIsAdmin check(isAdmin = 0 OR isAdmin = 1);
ALTER TABLE users ADD CONSTRAINT uniqueUsername unique(username);


/* Foreign keys */

ALTER TABLE attachments ADD CONSTRAINT fkAttachmentsMessagesChat FOREIGN KEY(fkMessage) REFERENCES messagesChat(id);

ALTER TABLE messagesChat ADD CONSTRAINT fkMessagesChatUsers FOREIGN KEY(fkUser) REFERENCES users(id);
ALTER TABLE messagesChat ADD CONSTRAINT fkMessagesChatChats FOREIGN KEY(fkChat) REFERENCES chats(id);

ALTER TABLE chats ADD CONSTRAINT fkChatsFriendships FOREIGN KEY(fkFriendship) REFERENCES friendships(id);

ALTER TABLE friendships ADD CONSTRAINT fkFriendshipsUsers1 FOREIGN KEY(fkUser1) REFERENCES users(id);
ALTER TABLE friendships ADD CONSTRAINT fkFriendshipsUsers2 FOREIGN KEY(fkUser2) REFERENCES users(id);

ALTER TABLE calls ADD CONSTRAINT fkCallsChats FOREIGN KEY(fkChat) REFERENCES chats(id);

ALTER TABLE registrations ADD CONSTRAINT fkRegistrationsUsers FOREIGN KEY(fkUser) REFERENCES users(id);
ALTER TABLE registrations ADD CONSTRAINT fkRegistrationsCommunities FOREIGN KEY(fkCommunity) REFERENCES communities(id);

ALTER TABLE communities ADD CONSTRAINT fkCommunitiesUsers FOREIGN KEY(fkUserOwner) REFERENCES users(id);

ALTER TABLE sections ADD CONSTRAINT fkSectionsCommunities FOREIGN KEY(fkCommunity) REFERENCES communities(id);

ALTER TABLE channels ADD CONSTRAINT fkChannelsSections FOREIGN KEY(fkSection) REFERENCES sections(id);

ALTER TABLE messagesCommunity ADD CONSTRAINT fkMessagesCommunityChannels FOREIGN KEY(fkChannel) REFERENCES channels(id);
ALTER TABLE messagesCommunity ADD CONSTRAINT fkMessagesCommunityUsers FOREIGN KEY(fkUser) REFERENCES users(id);


/* CONTROLLI */

describe attachments; 
describe calls; 
describe channels; 
describe chats; 
describe communities; 
describe friendships; 
describe messageschat; 
describe messagescommunity; 
describe registrations; 
describe sections; 
describe users;



SHOW CREATE TABLE attachments;
SHOW CREATE TABLE calls;
SHOW CREATE TABLE channels;
SHOW CREATE TABLE chats;
SHOW CREATE TABLE communities;
SHOW CREATE TABLE friendships;
SHOW CREATE TABLE messagesChat;
SHOW CREATE TABLE messagesCommunity;
SHOW CREATE TABLE registrations;
SHOW CREATE TABLE sections;
SHOW CREATE TABLE users;



select * from attachments;
select * from calls;
select * from channels;
select * from chats;
select * from communities;
select * from friendships;
select * from messagesChat;
select * from messagesCommunity;
select * from registrations;
select * from sections;
select * from users;
