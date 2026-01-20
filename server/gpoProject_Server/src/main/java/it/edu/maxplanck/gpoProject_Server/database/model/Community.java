package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "communities")
public final class Community {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkUserOwner", nullable = false)
	private User fkUserOwner;
	
	// Entity Fields
	@Column(name = "inviteCode", length = 20, nullable = false, unique = true)
	private String inviteCode;
	
	@Column(name = "isInviteCodeValid", columnDefinition = "isInviteCodeValid TINYINT(1) DEFAULT 1")
	private boolean isInviteCodeValid;
	
	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "createdAt", columnDefinition = "createdAt DATE DEFAULT CURDATE()", insertable = false, updatable = false)
	private LocalDate createdAt;

	public Community(){
		super();
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
