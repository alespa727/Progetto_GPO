package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.CommunityData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.RegistrationData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.UserData;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

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
        this.id = new RegistrationID(fkCommunity.getPkID(), fkUser.getId());
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