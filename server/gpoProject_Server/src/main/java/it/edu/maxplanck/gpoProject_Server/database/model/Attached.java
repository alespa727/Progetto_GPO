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
	@Column(name = AttachedData.columnNamePath, nullable = false, length = AttachedData.pathLenght)
	private String path;

	public Attached() {
		super();
	}

	public Attached(Integer pkID, MessageChat fkMessage, String path) {
		super();
		this.pkID = pkID;
		this.fkMessage = fkMessage;
		this.path = path;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
