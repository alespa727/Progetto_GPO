package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.AttachedChatData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.MessageChatData;
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
@Table(name = AttachedChatData.tableName)
public class AttachedChat {
	
	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = AttachedChatData.columnNamePrimaryKey, columnDefinition = AttachedChatData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = AttachedChatData.columnNameFkMessage, nullable = false, columnDefinition = MessageChatData.typePK, foreignKey = @ForeignKey(name = AttachedChatData.constraintNameFkMessage))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private MessageChat fkMessage;
	
	// Entity Fields
	@Column(name = AttachedChatData.columnNameFilename, length = AttachedChatData.filenameLenght)
	private String filename;
	
	@Column(name = AttachedChatData.columnNameExtension, length = AttachedChatData.extensionLenght)
	private String extension;
	
	public AttachedChat() {
		super();
	}

	public AttachedChat(MessageChat fkMessage, String filename, String extension) {
		super();
		this.pkID = null;
		this.fkMessage = fkMessage;
		this.filename = filename;
		this.extension = extension;
	}
	
	public AttachedChat(Integer pkID, MessageChat fkMessage, String filename, String extension) {
		super();
		this.pkID = pkID;
		this.fkMessage = fkMessage;
		this.filename = filename;
		this.extension = extension;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public MessageChat getFkMessage() {
		return fkMessage;
	}

	public void setFkMessage(MessageChat fkMessage) {
		this.fkMessage = fkMessage;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
