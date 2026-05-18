package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.ChatData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.FriendshipData;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = ChatData.tableName)
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// Foreign Keys
	@OneToOne
	@JoinColumn(name = ChatData.columnNameFkFriendship, nullable = false, foreignKey = @ForeignKey(name = ChatData.constraintNameFkFriendship))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Friendship fkFriendship;
	
	// Entity Fields
	@Column(name = ChatData.columnNameTimeLastMessage)
	private LocalDateTime timeLastMessage;
	
	@Column(nullable = false)
	private LocalDate createdAt;

	public Chat(){
		super();
	}
	
	public Chat(Friendship fkFriendship) {
		super();
		this.id = null;
		this.fkFriendship = fkFriendship;
		this.timeLastMessage = null;
		this.createdAt = LocalDate.now();
	}
	
	public Chat(Integer id, Friendship fkFriendship, LocalDateTime timeLastMessage, LocalDate createdAt) {
		super();
		this.id = id;
		this.fkFriendship = fkFriendship;
		this.timeLastMessage = timeLastMessage;
		this.createdAt = createdAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer pkID) {
		this.id = pkID;
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
