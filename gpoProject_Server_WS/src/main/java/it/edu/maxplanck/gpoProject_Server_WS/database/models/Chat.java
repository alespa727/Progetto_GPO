package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.ChatData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.FriendshipData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = ChatData.tableName)
public final class Chat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ChatData.columnNamePrimaryKey, columnDefinition = ChatData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@OneToOne
	@JoinColumn(name = ChatData.columnNameFkFriendship, nullable = false, columnDefinition = FriendshipData.typePK, foreignKey = @ForeignKey(name = ChatData.constraintNameFkFriendship))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Friendship fkFriendship;
	
	// Entity Fields
	@Column(name = ChatData.columnNameTimeLastMessage)
	private LocalDateTime timeLastMessage;
	
	@Column(nullable = false)
	private LocalDate createdAt;

	@OneToMany(mappedBy = "fkChat", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CallChat> calls;
	
	@OneToMany(mappedBy = "fkChat", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MessageChat> messages;
	
	public Chat(){
		super();
	}
	
	public Chat(Friendship fkFriendship) {
		super();
		this.pkID = null;
		this.fkFriendship = fkFriendship;
		this.timeLastMessage = null;
		this.createdAt = LocalDate.now();
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

	public List<CallChat> getCalls() {
		return calls;
	}

	public void setCalls(List<CallChat> calls) {
		this.calls = calls;
	}

	public List<MessageChat> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageChat> messages) {
		this.messages = messages;
	}
}
