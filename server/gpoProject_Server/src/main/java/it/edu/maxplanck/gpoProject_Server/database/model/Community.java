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
	
	@Column(name = "createdAt")
	private LocalDate createdAt;
	
}
