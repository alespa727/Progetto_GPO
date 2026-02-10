package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.UserData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe che rappresenta l'entita' nel database degli utenti
 */
@Entity
@Table(name = UserData.tableName)
public final class User {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = UserData.columnNamePrimaryKey)
	private Integer pkID;
	
	// Foreign Keys
	
	
	// Entity Fields
	@Column(name = UserData.columnNameUsername, length = UserData.usernameLength, nullable = false, unique = true)
	private String username;
	
	@Column(name = UserData.columnNamePassword, length = UserData.passwordLength, nullable = false)
	private String password;
	
	@Column(name = UserData.columnNameIsAdmin, columnDefinition = "isAdmin TINYINT(1) DEFAULT 0", insertable = false)
	private Boolean isAdmin;
	
	@Column(name = UserData.columnNameTimeLastAccess)
	private LocalDateTime timeLastAccess;
	
	@Column(nullable = false)
	private LocalDate createdAt;
	
	@Column(name = UserData.columnNameImagePath, length = UserData.imagePathLenght)
	private String imagePath;

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

	public Boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(Boolean isAdmin) {
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
}
