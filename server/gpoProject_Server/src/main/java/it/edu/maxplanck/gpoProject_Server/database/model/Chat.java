package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chats")
public final class Chat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@OneToOne
	@JoinColumn(name = "fkFriendship", nullable = false)
	private Friendship fkFriendship;
	
	// Entity Fields
	@Column(name = "timeLastMessage")
	private LocalDateTime timeLastMessage;
	
	@Column(name = "createdAt", columnDefinition = "createdAt DATE DEFAULT CURDATE()")
	private LocalDate createdAt;
	
}
