package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "registrations")
@IdClass(RegistrationID.class)
public final class Registration {

	// Primary Keys
	@Id
	@ManyToOne
	@JoinColumn(name = "fkCommunity", nullable = false)
    private Community fkCommunity;

	@Id
	@ManyToOne
	@JoinColumn(name = "fkUser", nullable = false)
    private User fkUser;
    
    // Foreign Keys
    
    
    // Entity Fields
	@Column(name = "date", columnDefinition = "date DATETIME DEFAULT CURRENT_TIMESTAMP()")
    private LocalDateTime date;
    
}