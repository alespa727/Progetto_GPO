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
	
	@Column(name = "isAdmin", columnDefinition = "isAdmin TINYINT(1) DEFAULT 0")
	private boolean isAdmin;
	
	@Column(name = "timeLastAccess", columnDefinition = "timeLastAccess DATETIME DEFAULT CURRENT_TIMESTAMP()")
	private LocalDateTime timeLastAccess;
	
	@Column(name = "createdAt", columnDefinition = "createdAt DATE DEFAULT CURDATE()")
	private LocalDate createdAt;
	
	@Column(name = "imagePath", length = 100)
	private String imagePath;
	
}
