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
	
	@Column(name = "sentAt", columnDefinition = "sentAt DATETIME DEFAULT CURRENT_TIMESTAMP()")
	private LocalDateTime sentAt;
	
}
