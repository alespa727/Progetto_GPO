package it.edu.maxplanck.gpoProject_Server.database.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = ModelDataDatabase.ChannelData.tableName)
public class Channel {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ModelDataDatabase.ChannelData.columnNamePrimaryKey, columnDefinition = ModelDataDatabase.ChannelData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = ModelDataDatabase.ChannelData.columnNameFkSection, nullable = false, columnDefinition = ModelDataDatabase.SectionData.typePK, foreignKey = @ForeignKey(name = ModelDataDatabase.ChannelData.constraintNameFkSection))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Section fkSection;
	
	// Entity Fields
	@Column(name = ModelDataDatabase.ChannelData.columnNameName, length = ModelDataDatabase.ChannelData.nameLenght, nullable = false)
	private String name;
	
	@Column(name = ModelDataDatabase.ChannelData.columnNameType, nullable = false)
	private String type;
	
	@Column(name = ModelDataDatabase.ChannelData.columnNameDescription)
	private String description;
	
	@Column(nullable = false)
	private LocalDate createdAt;

	public Channel(){
		super();
	}
	
	public Channel(Section fkSection, String name, String type, String description) {
		super();
		this.pkID = null;
		this.fkSection = fkSection;
		this.name = name;
		this.type = type;
		this.description = description;
		this.createdAt = LocalDate.now();
	}
	
	public Channel(Integer pkID, Section fkSection, String name, String type, String description, LocalDate createdAt) {
		super();
		this.pkID = pkID;
		this.fkSection = fkSection;
		this.name = name;
		this.type = type;
		this.description = description;
		this.createdAt = createdAt;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public Section getFkSection() {
		return fkSection;
	}

	public void setFkSection(Section fkSection) {
		this.fkSection = fkSection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
}

