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
@Table(name = "channels")
public final class Channel {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkSection", nullable = false)
	private Section fkSection;
	
	// Entity Fields
	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "type", length = 20, nullable = false)
	private String type;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "createdAt", columnDefinition = "createdAt DATE DEFAULT CURDATE()")
	private LocalDate createdAt;
	
}
