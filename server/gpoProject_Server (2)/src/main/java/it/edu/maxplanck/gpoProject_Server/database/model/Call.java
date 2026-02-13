package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDateTime;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.CallData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Classe che rappresenta l'entita' nel database delle chiamate
 */
@Entity
@Table(name = CallData.tableName)
public final class Call {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = CallData.columnNamePrimaryKey)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = CallData.columnNameFkChat, nullable = false)
	private Chat fkChat;
	
	// Entity Fields
	@Column(name = CallData.columnNameStartTime, columnDefinition = "startTime DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime startTime;
	
	@Column(name = CallData.columnNameEndTime)
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
