package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.ChatData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = ChatData.tableName)
public final class Chat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ChatData.columnNamePrimaryKey)
	private Integer pkID;
	
	// Foreign Keys
	@OneToOne
	@JoinColumn(name = ChatData.columnNameFkFriendship, nullable = false)
	private Friendship fkFriendship;
	
	// Entity Fields
	@Column(name = ChatData.columnNameTimeLastMessage)
	private LocalDateTime timeLastMessage;
	
	@Column(name = ChatData.columnNamecreatedAt, columnDefinition = "createdAt DATE DEFAULT CURDATE()", insertable = false, updatable = false)
	private LocalDate createdAt;

	public Chat(){
		super();
	}
	
	public Chat(Integer pkID, Friendship fkFriendship, LocalDateTime timeLastMessage, LocalDate createdAt) {
		super();
		this.pkID = pkID;
		this.fkFriendship = fkFriendship;
		this.timeLastMessage = timeLastMessage;
		this.createdAt = createdAt;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public Friendship getFkFriendship() {
		return fkFriendship;
	}

	public void setFkFriendship(Friendship fkFriendship) {
		this.fkFriendship = fkFriendship;
	}

	public LocalDateTime getTimeLastMessage() {
		return timeLastMessage;
	}

	public void setTimeLastMessage(LocalDateTime timeLastMessage) {
		this.timeLastMessage = timeLastMessage;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
}
