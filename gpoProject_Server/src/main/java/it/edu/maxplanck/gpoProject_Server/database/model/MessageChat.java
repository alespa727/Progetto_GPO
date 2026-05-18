package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.ChatData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.MessageChatData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.UserData;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = MessageChatData.tableName)
public class MessageChat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = MessageChatData.columnNameFkChat, nullable = false, foreignKey = @ForeignKey(name = MessageChatData.constraintNameFkChat))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Chat fkChat;
	
	@ManyToOne
	@JoinColumn(name = MessageChatData.columnNameFkUser, nullable = false, foreignKey = @ForeignKey(name = MessageChatData.constraintNameFkUser))
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
		this.id = null;
		this.fkChat = fkChat;
		this.fkUser = fkUser;
		this.message = message;
		this.sentAt = null;
	}
	
	public MessageChat(Integer id, Chat fkChat, User fkUser, String message, LocalDateTime sentAt) {
		super();
		this.id = id;
		this.fkChat = fkChat;
		this.fkUser = fkUser;
		this.message = message;
		this.sentAt = sentAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer pkID) {
		this.id = pkID;
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
