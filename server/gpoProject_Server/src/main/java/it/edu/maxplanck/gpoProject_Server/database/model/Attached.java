package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.AttachedData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Classe che rappresenta l'entita' nel database degli allegati
 */
@Entity
@Table(name = AttachedData.tableName)
public final class Attached {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = AttachedData.columnNamePrimaryKey)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = AttachedData.columnNameFkMessage, nullable = false)
	private MessageChat fkMessage;
	
	// Entity Fields
	@Column(name = AttachedData.columnNameFilename, length = AttachedData.filenameLenght)
	private String filename;
	
	@Column(name = AttachedData.columnNameExtension, length = AttachedData.extensionLenght)
	private String extension;
	
	public Attached() {
		super();
	}

	public Attached(MessageChat fkMessage, String filename, String extension) {
		super();
		this.pkID = null;
		this.fkMessage = fkMessage;
		this.filename = filename;
		this.extension = extension;
	}
	
	public Attached(Integer pkID, MessageChat fkMessage, String filename, String extension) {
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
