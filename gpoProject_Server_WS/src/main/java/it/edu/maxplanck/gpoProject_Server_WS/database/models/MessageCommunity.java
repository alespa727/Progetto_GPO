package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.ChannelData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.MessageCommunityData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.UserData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = MessageCommunityData.tableName)
public final class MessageCommunity {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = MessageCommunityData.columnNamePrimaryKey, columnDefinition = MessageCommunityData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = MessageCommunityData.columnNameFkChannel, nullable = false, columnDefinition = ChannelData.typePK, foreignKey = @ForeignKey(name = MessageCommunityData.constraintNameFkChannel))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Channel fkChannel;
	
	@ManyToOne
	@JoinColumn(name = MessageCommunityData.columnNameFkUser, nullable = false, columnDefinition = UserData.typePK, foreignKey = @ForeignKey(name = MessageCommunityData.constraintNameFkUser))
	private User fkUser;
	
	// Entity Fields
	@Column(name = MessageCommunityData.columnNameMessage)
	private String message;
	
	@Column(name = MessageCommunityData.columnNameSentAt, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
	private LocalDateTime sentAt;

	@OneToMany(mappedBy = "fkMessage", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AttachedCommunity> attachments;
	
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

	public List<AttachedCommunity> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachedCommunity> attachments) {
		this.attachments = attachments;
	}
}

