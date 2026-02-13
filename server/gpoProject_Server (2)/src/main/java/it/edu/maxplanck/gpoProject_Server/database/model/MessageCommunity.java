package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDateTime;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.MessageCommunityData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Classe che rappresenta l'entita' nel database dei messaggi di un canale di una community
 */
@Entity
@Table(name = MessageCommunityData.tableName)
public final class MessageCommunity {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = MessageCommunityData.columnNamePrimaryKey)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = MessageCommunityData.columnNameFkChannel, nullable = false)
	private Channel fkChannel;
	
	@ManyToOne
	@JoinColumn(name = MessageCommunityData.columnNameFkUser, nullable = false)
	private User fkUser;
	
	// Entity Fields
	@Column(name = MessageCommunityData.columnNameMessage)
	private String message;
	
	@Column(name = MessageCommunityData.columnNameSentAt, columnDefinition = "sentAt DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime sentAt;

	public MessageCommunity(){
		super();
	}
	
	public MessageCommunity(Channel fkChannel, User fkUser, String message) {
		super();
		this.pkID = null;
		this.fkChannel = fkChannel;
		this.fkUser = fkUser;
		this.message = message;
		this.sentAt = null;
	}
	
	public MessageCommunity(Integer pkID, Channel fkChannel, User fkUser, String message, LocalDateTime sentAt) {
		super();
		this.pkID = pkID;
		this.fkChannel = fkChannel;
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

	public Channel getFkChannel() {
		return fkChannel;
	}

	public void setFkChannel(Channel fkChannel) {
		this.fkChannel = fkChannel;
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
