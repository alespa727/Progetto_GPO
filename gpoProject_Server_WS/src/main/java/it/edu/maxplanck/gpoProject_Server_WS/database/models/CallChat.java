package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.CallChatData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.ChatData;
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
@Table(name = CallChatData.tableName)
public final class CallChat {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = CallChatData.columnNamePrimaryKey, columnDefinition = CallChatData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = CallChatData.columnNameFkChat, nullable = false, columnDefinition = ChatData.typePK, foreignKey = @ForeignKey(name = CallChatData.constraintNameFkChat))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Chat fkChat;
	
	// Entity Fields
	@Column(name = CallChatData.columnNameStartTime, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime startTime;
	
	@Column(name = CallChatData.columnNameEndTime)
	private LocalDateTime endTime;

	public CallChat(){
		super();
	}
	
	public CallChat(Chat fkChat) {
		super();
		this.pkID = null;
		this.fkChat = fkChat;
		this.startTime = null;
		this.endTime = null;
	}
	
	public CallChat(Integer pkID, Chat fkChat, LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.pkID = pkID;
		this.fkChat = fkChat;
		this.startTime = startTime;
		this.endTime = endTime;
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

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
}
