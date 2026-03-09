package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.AttachedCommunityData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.MessageCommunityData;
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
@Table(name = AttachedCommunityData.tableName)
public class AttachedCommunity {
	
	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = AttachedCommunityData.columnNamePrimaryKey, columnDefinition = AttachedCommunityData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = AttachedCommunityData.columnNameFkMessage, nullable = false, columnDefinition = MessageCommunityData.typePK, foreignKey = @ForeignKey(name = AttachedCommunityData.constraintNameFkMessage))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private MessageCommunity fkMessage;
	
	// Entity Fields
	@Column(name = AttachedCommunityData.columnNameFilename, length = AttachedCommunityData.filenameLenght)
	private String filename;
	
	@Column(name = AttachedCommunityData.columnNameExtension, length = AttachedCommunityData.extensionLenght)
	private String extension;
	
	public AttachedCommunity() {
		super();
	}

	public AttachedCommunity(MessageCommunity fkMessage, String filename, String extension) {
		super();
		this.pkID = null;
		this.fkMessage = fkMessage;
		this.filename = filename;
		this.extension = extension;
	}
	
	public AttachedCommunity(Integer pkID, MessageCommunity fkMessage, String filename, String extension) {
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

	public MessageCommunity getFkMessage() {
		return fkMessage;
	}

	public void setFkMessage(MessageCommunity fkMessage) {
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
