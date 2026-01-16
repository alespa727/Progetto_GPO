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
@Table(name = "friendships")
public final class Friendship {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkUser1", nullable = false)
	private User fkUser1;
	
	@ManyToOne
	@JoinColumn(name = "fkUser2", nullable = false)
	private User fkUser2;
	
	// Entity Fields
	@Column(name = "createdAt", columnDefinition = "createdAt DATE DEFAULT CURDATE()")
	private LocalDate createdAt;
	
}
