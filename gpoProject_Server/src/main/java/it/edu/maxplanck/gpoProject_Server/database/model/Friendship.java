package it.edu.maxplanck.gpoProject_Server.database.model;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.FriendshipData;
import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase.UserData;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = FriendshipData.tableName)
public class Friendship {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = FriendshipData.columnNamePrimaryKey, columnDefinition = FriendshipData.typePK)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = FriendshipData.columnNameFkUser1, nullable = false, columnDefinition = UserData.typePK, foreignKey = @ForeignKey(name = FriendshipData.constraintNameFkUser1))
	private User fkUser1;
	
	@ManyToOne
	@JoinColumn(name = FriendshipData.columnNameFkUser2, nullable = false, columnDefinition = UserData.typePK, foreignKey = @ForeignKey(name = FriendshipData.constraintNameFkUser2))
	private User fkUser2;
	
	@Column(nullable = false)
	private LocalDate createdAt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accepted = false;

	public Friendship(){
		super();
	}
	
	public Friendship(User fkUser1, User fkUser2) {
		super();
		this.pkID = null;
		this.fkUser1 = fkUser1;
		this.fkUser2 = fkUser2;
		this.createdAt = LocalDate.now();
	}
	
	public Friendship(Integer pkID, User fkUser1, User fkUser2, LocalDate createdAt) {
		super();
		this.pkID = pkID;
		this.fkUser1 = fkUser1;
		this.fkUser2 = fkUser2;
		this.createdAt = createdAt;
	}

	public Integer getPkID() {
		return pkID;
	}

	public void setPkID(Integer pkID) {
		this.pkID = pkID;
	}

	public User getFkUser1() {
		return fkUser1;
	}

	public void setFkUser1(User fkUser1) {
		this.fkUser1 = fkUser1;
	}

	public User getFkUser2() {
		return fkUser2;
	}

	public void setFkUser2(User fkUser2) {
		this.fkUser2 = fkUser2;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
