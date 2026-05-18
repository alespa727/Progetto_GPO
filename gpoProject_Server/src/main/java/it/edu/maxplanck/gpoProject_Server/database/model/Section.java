package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.CommunityData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.SectionData;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = SectionData.tableName)
public class Section {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = SectionData.columnNamePrimaryKey, columnDefinition = SectionData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = SectionData.columnNameFkCommunity, nullable = false, columnDefinition = CommunityData.typePK, foreignKey = @ForeignKey(name = SectionData.constraintNameFkCommunity))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Community fkCommunity;
	
	// Entity Fields
	@Column(name = SectionData.columnNameName, length = SectionData.nameLenght, nullable = false)
	private String name;

	public Section(){
		super();
	}

	public Section(Community fkCommunity, String name) {
		super();
		this.pkID = null;
		this.fkCommunity = fkCommunity;
		this.name = name;
	}
	
	public Section(Integer pkID, Community fkCommunity, String name) {
		super();
		this.pkID = pkID;
		this.fkCommunity = fkCommunity;
		this.name = name;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public Community getFkCommunity() {
		return fkCommunity;
	}

	public void setFkCommunity(Community fkCommunity) {
		this.fkCommunity = fkCommunity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
