package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messagesChat")
public final class MessageChat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkChat", nullable = false)
	private Chat fkChat;
	
	@ManyToOne
	@JoinColumn(name = "fkUser", nullable = false)
	private User fkUser;
	
	// Entity Fields
	@Column(name = "message")
	private String message;
	
	@Column(name = "sentAt", columnDefinition = "sentAt DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime sentAt;

	public MessageChat(){
		super();
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
