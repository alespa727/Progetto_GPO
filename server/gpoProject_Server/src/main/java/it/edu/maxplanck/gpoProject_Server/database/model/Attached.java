package it.edu.maxplanck.gpoProject_Server.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attachments")
public final class Attached {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = "fkMessage", nullable = false)
	private MessageChat fkMessage;
	
	// Entity Fields
	@Column(name = "path", nullable = false, length = 255)
	private String path;
}
