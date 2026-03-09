package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.ChannelData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.SectionData;
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
@Table(name = ChannelData.tableName)
public final class Channel {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ChannelData.columnNamePrimaryKey, columnDefinition = ChannelData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = ChannelData.columnNameFkSection, nullable = false, columnDefinition = SectionData.typePK, foreignKey = @ForeignKey(name = ChannelData.constraintNameFkSection))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Section fkSection;
	
	// Entity Fields
	@Column(name = ChannelData.columnNameName, length = ChannelData.nameLenght, nullable = false)
	private String name;
	
	@Column(name = ChannelData.columnNameType, nullable = false)
	private String type;
	
	@Column(name = ChannelData.columnNameDescription)
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

