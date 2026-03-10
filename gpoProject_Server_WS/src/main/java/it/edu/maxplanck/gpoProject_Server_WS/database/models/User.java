package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.UserData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = UserData.tableName)
public final class User {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = UserData.columnNamePrimaryKey, columnDefinition = UserData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	
	
	// Entity Fields
	@Column(name = UserData.columnNameUsername, length = UserData.usernameLength, nullable = false, unique = true)
	private String username;
	
	@Column(name = UserData.columnNamePassword, length = UserData.passwordLength, nullable = false)
	private String password;
	
	@Column(name = UserData.columnNameIsAdmin, columnDefinition = "TINYINT(1) DEFAULT 0", insertable = false)
	private Boolean isAdmin;
	
	@Column(name = UserData.columnNameTimeLastAccess)
	private LocalDateTime timeLastAccess;
	
	@Column(nullable = false)
	private LocalDate createdAt;
	
	@Column(name = UserData.columnNameImagePath, length = UserData.imagePathLenght)
	private String imagePath;

	@OneToMany(mappedBy = "fkUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MessageChat> messagesChats;
	
	@OneToMany(mappedBy = "fkUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MessageCommunity> messagesCommunities;
	
	@OneToMany(mappedBy = "fkUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Registration> registrations;
	
	@OneToMany(mappedBy = "fkUserOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Community> communitiesOwner;
	
	public User(){
		super();
	}
	
	public User(String username, String password) {
		super();
		this.pkID = null;
		this.username = username;
		this.password = password;
		this.isAdmin = null;
		this.timeLastAccess = null;
	    this.createdAt = LocalDate.now();
		this.imagePath = null;
	}
	
	public User(Integer pkID, String username, String password, Boolean isAdmin, LocalDateTime timeLastAccess, LocalDate createdAt, String imagePath) {
		super();
		this.pkID = pkID;
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
		this.timeLastAccess = timeLastAccess;
		this.createdAt = createdAt;
		this.imagePath = imagePath;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public LocalDateTime getTimeLastAccess() {
		return timeLastAccess;
	}

	public void setTimeLastAccess(LocalDateTime timeLastAccess) {
		this.timeLastAccess = timeLastAccess;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<MessageChat> getMessagesChats() {
		return messagesChats;
	}

	public void setMessagesChats(List<MessageChat> messagesChats) {
		this.messagesChats = messagesChats;
	}

	public List<MessageCommunity> getMessagesCommunities() {
		return messagesCommunities;
	}

	public void setMessagesCommunities(List<MessageCommunity> messagesCommunities) {
		this.messagesCommunities = messagesCommunities;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public List<Community> getCommunitiesOwner() {
		return communitiesOwner;
	}

	public void setCommunitiesOwner(List<Community> communitiesOwner) {
		this.communitiesOwner = communitiesOwner;
	}
}
