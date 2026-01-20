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
@Table(name = "calls")
public final class Call {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkChat", nullable = false)
	private Chat fkChat;
	
	// Entity Fields
	@Column(name = "startTime", columnDefinition = "startTime DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime startTime;
	
	@Column(name = "endTime")
	private LocalDateTime endTime;

	public Call(){
		super();
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
