package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDateTime;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.RegistrationData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Classe che rappresenta l'entita' nel database la iscrizione ad una community
 */
@Entity
@Table(name = RegistrationData.tableName)
@IdClass(RegistrationID.class)
public final class Registration {

	// Primary Keys
	// Foreign Keys
	@Id
	@ManyToOne
	@JoinColumn(name = RegistrationData.columnNameFkCommunity, nullable = false)
    private Community fkCommunity;

	@Id
	@ManyToOne
	@JoinColumn(name = RegistrationData.columnNameFkUser, nullable = false)
    private User fkUser;
    
    // Entity Fields
	@Column(name = RegistrationData.columnNameDate, columnDefinition = "date DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
    private LocalDateTime date;

	public Registration(){
		super();
	}
	
	public Registration(Community fkCommunity, User fkUser) {
		super();
		this.fkCommunity = fkCommunity;
		this.fkUser = fkUser;
		this.date = null;
	}
	
	public Registration(Community fkCommunity, User fkUser, LocalDateTime date) {
		super();
		this.fkCommunity = fkCommunity;
		this.fkUser = fkUser;
		this.date = date;
	}

	public Community getFkCommunity() {
		return fkCommunity;
	}

	public void setFkCommunity(Community fkCommunity) {
		this.fkCommunity = fkCommunity;
	}

	public User getFkUser() {
		return fkUser;
	}

	public void setFkUser(User fkUser) {
		this.fkUser = fkUser;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}