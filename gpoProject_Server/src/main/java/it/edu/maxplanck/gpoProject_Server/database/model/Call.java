package it.edu.maxplanck.gpoProject_Server.database.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = ModelDataDatabase.CallChatData.tableName)
public  class Call {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ModelDataDatabase.CallChatData.columnNamePrimaryKey, columnDefinition = ModelDataDatabase.CallChatData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = ModelDataDatabase.CallChatData.columnNameFkChat, nullable = false, columnDefinition = ModelDataDatabase.ChatData.typePK, foreignKey = @ForeignKey(name = ModelDataDatabase.CallChatData.constraintNameFkChat))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Chat fkChat;
	
	// Entity Fields
	@Column(name = ModelDataDatabase.CallChatData.columnNameStartTime, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime startTime;
	
	@Column(name = ModelDataDatabase.CallChatData.columnNameEndTime)
	private LocalDateTime endTime;

	public Call(){
		super();
	}
	
	public Call(Chat fkChat) {
		super();
		this.pkID = null;
		this.fkChat = fkChat;
		this.startTime = null;
		this.endTime = null;
	}
	
	public Call(Integer pkID, Chat fkChat, LocalDateTime startTime, LocalDateTime endTime) {
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
