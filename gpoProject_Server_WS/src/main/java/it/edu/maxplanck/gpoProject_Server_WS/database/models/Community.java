package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDate;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.CommunityData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.UserData;
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
@Table(name = CommunityData.tableName)
public final class Community {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = CommunityData.columnNamePrimaryKey, columnDefinition = CommunityData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = CommunityData.columnNameFkUserOwner, nullable = false, columnDefinition = UserData.typePK, foreignKey = @ForeignKey(name = CommunityData.constraintNameFkUser))
	private User fkUserOwner;
	
	// Entity Fields
	@Column(name = CommunityData.columnNameInviteCode, length = CommunityData.inviteCodeLenght, nullable = false, unique = true)
	private String inviteCode;
	
	@Column(name = CommunityData.columnNameIsInviteCodeValid, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isInviteCodeValid;
	
	@Column(name = CommunityData.columnNameName, length = CommunityData.nameLenght, nullable = false)
	private String name;
	
	@Column(name = CommunityData.columnNameDescription)
	private String description;
	
	@Column(nullable = false)
	private LocalDate createdAt;

	public Community(){
		super();
	}
	
	public Community(User fkUserOwner, String inviteCode, boolean isInviteCodeValid, String name, String description) {
		super();
		this.pkID = null;
		this.fkUserOwner = fkUserOwner;
		this.inviteCode = inviteCode;
		this.isInviteCodeValid = isInviteCodeValid;
		this.name = name;
		this.description = description;
		this.createdAt = LocalDate.now();
	}
	
	public Community(Integer pkID, User fkUserOwner, String inviteCode, boolean isInviteCodeValid, String name, String description, LocalDate createdAt) {
		super();
		this.pkID = pkID;
		this.fkUserOwner = fkUserOwner;
		this.inviteCode = inviteCode;
		this.isInviteCodeValid = isInviteCodeValid;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public User getFkUserOwner() {
		return fkUserOwner;
	}

	public void setFkUserOwner(User fkUserOwner) {
		this.fkUserOwner = fkUserOwner;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public boolean isInviteCodeValid() {
		return isInviteCodeValid;
	}

	public void setInviteCodeValid(boolean isInviteCodeValid) {
		this.isInviteCodeValid = isInviteCodeValid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
