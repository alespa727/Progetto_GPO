package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.AttachedChatData;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = AttachedChatData.tableName)
public class AttachedChat {
	
	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = AttachedChatData.columnNameFkMessage, nullable = false, foreignKey = @ForeignKey(name = AttachedChatData.constraintNameFkMessage))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private MessageChat fkMessage;
	
	// Entity Fields
	@Column(name = AttachedChatData.columnNameFilename)
	private String filename;
    @Column(name = "originalname")
    private String originalname;
	
	@Column(name = AttachedChatData.columnNameExtension, length = AttachedChatData.extensionLenght)
	private String extension;

    public AttachedChat() {}

	public AttachedChat(MessageChat fkMessage, String filename, String originalname, String extension) {
		super();
        this.originalname = originalname;
        this.id = null;
		this.fkMessage = fkMessage;
		this.filename = filename;
		this.extension = extension;
	}
	
	public AttachedChat(Integer pkID, MessageChat fkMessage, String filename, String originalname, String extension) {
		super();
		this.id = pkID;
		this.fkMessage = fkMessage;
		this.filename = filename;
        this.originalname = originalname;
        this.extension = extension;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer pkID) {
		this.id = pkID;
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

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }
}
