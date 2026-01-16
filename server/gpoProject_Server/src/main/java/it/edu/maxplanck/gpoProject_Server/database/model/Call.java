package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "calls")
public final class Call {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkChat", nullable = false)
	private Chat fkChat;
	
	// Entity Fields
	@Column(name = "startTime", columnDefinition = "startTime DATETIME DEFAULT CURRENT_TIMESTAMP()")
	private LocalDateTime startTime;
	
	@Column(name = "endTime")
	private LocalDateTime endTime;
	
}
