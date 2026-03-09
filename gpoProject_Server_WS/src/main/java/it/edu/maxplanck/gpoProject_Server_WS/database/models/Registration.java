package it.edu.maxplanck.gpoProject_Server_WS.database.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.CommunityData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.RegistrationData;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.ModelDataDatabase.UserData;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = RegistrationData.tableName)
public final class Registration {

	// Primary Keys
	@EmbeddedId
    private RegistrationID id;
	
	// Foreign Keys
	@ManyToOne
	@MapsId("idCommunity")
	@JoinColumn(name = RegistrationData.columnNameFkCommunity, nullable = false, columnDefinition = CommunityData.typePK, foreignKey = @ForeignKey(name = RegistrationData.constraintNameFkCommunity))
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Community fkCommunity;

	@ManyToOne
	@MapsId("idUser")
	@JoinColumn(name = RegistrationData.columnNameFkUser, nullable = false, columnDefinition = UserData.typePK, foreignKey = @ForeignKey(name = RegistrationData.constraintNameFkUser))
    @OnDelete(action = OnDeleteAction.CASCADE)
	private User fkUser;
    
    // Entity Fields
	@Column(name = RegistrationData.columnNameDate, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP()", insertable = false, updatable = false)
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