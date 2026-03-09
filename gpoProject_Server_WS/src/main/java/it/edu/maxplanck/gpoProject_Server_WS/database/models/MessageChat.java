package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.ChatData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.MessageChatData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.UserData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = MessageChatData.tableName)
public final class MessageChat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = MessageChatData.columnNamePrimaryKey, columnDefinition = MessageChatData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = MessageChatData.columnNameFkChat, nullable = false, columnDefinition = ChatData.typePK, foreignKey = @ForeignKey(name = MessageChatData.constraintNameFkChat))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Chat fkChat;
	
	@ManyToOne
	@JoinColumn(name = MessageChatData.columnNameFkUser, nullable = false, columnDefinition = UserData.typePK, foreignKey = @ForeignKey(name = MessageChatData.constraintNameFkUser))
	private User fkUser;
	
	// Entity Fields
	@Column(name = MessageChatData.columnNameMessage)
	private String message;
	
	@Column(name = MessageChatData.columnNameSentAt, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime sentAt;

	public MessageChat(){
		super();
	}
	
	public MessageChat(Chat fkChat, User fkUser, String message) {
		super();
		this.pkID = null;
		this.fkChat = fkChat;
		this.fkUser = fkUser;
		this.message = message;
		this.sentAt = null;
	}
	
	public MessageChat(Integer pkID, Chat fkChat, User fkUser, String message, LocalDateTime sentAt) {
		super();
		this.pkID = pkID;
		this.fkChat = fkChat;
		this.fkUser = fkUser;
		this.message = message;
		this.sentAt = sentAt;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public Chat getFkChat() {
		return fkChat;
	}

	public void setFkChat(Chat fkChat) {
		this.fkChat = fkChat;
	}

	public User getFkUser() {
		return fkUser;
	}

	public void setFkUser(User fkUser) {
		this.fkUser = fkUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}
