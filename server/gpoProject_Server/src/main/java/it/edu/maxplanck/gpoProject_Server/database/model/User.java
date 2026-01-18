package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public final class User {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	
	
	// Entity Fields
	@Column(name = "username", length = 20, nullable = false, unique = true)
	private String username;
	
	@Column(name = "password", length = 256, nullable = false)
	private String password;
	
	@Column(name = "isAdmin", columnDefinition = "isAdmin TINYINT(1) DEFAULT 0", insertable = false)
	private Boolean isAdmin;
	
	@Column(name = "timeLastAccess")
	private LocalDateTime timeLastAccess;
	
	@Column(name = "createdAt", columnDefinition = "createdAt DATE DEFAULT CURDATE()", insertable = false, updatable = false)
	private LocalDate createdAt;
	
	@Column(name = "imagePath", length = 100)
	private String imagePath;

	public User(){
		super();
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
