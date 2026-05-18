package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.AttachedCommunityData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.MessageCommunityData;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "originalname")
    private String originalname;
	
	@Column(name = AttachedCommunityData.columnNameExtension, length = AttachedCommunityData.extensionLenght)
	private String extension;


	public AttachedCommunity(MessageCommunity fkMessage, String filename, String originalname, String extension) {
		super();
        this.originalname = originalname;
        this.pkID = null;
		this.fkMessage = fkMessage;
		this.filename = filename;
		this.extension = extension;
	}
	
	public AttachedCommunity(Integer pkID, MessageCommunity fkMessage, String filename, String originalname, String extension) {
		super();
		this.pkID = pkID;
		this.fkMessage = fkMessage;
		this.filename = filename;
        this.originalname = originalname;
        this.extension = extension;
	}

    public AttachedCommunity() {

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

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }
}
