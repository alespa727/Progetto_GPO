package it.edu.maxplanck.gpoProject_Server.database.model;

import java.time.LocalDate;

import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.FriendshipData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = FriendshipData.tableName)
public final class Friendship {

	// Primary Keys
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = FriendshipData.columnNamePrimaryKey)
	private Integer pkID;
	
	// Foreign Keys
	@ManyToOne
	@JoinColumn(name = FriendshipData.columnNameFkUser1, nullable = false)
	private User fkUser1;
	
	@ManyToOne
	@JoinColumn(name = FriendshipData.columnNameFkUser2, nullable = false)
	private User fkUser2;
	
	// Entity Fields
	@Column(name = FriendshipData.columnNameCreatedAt, columnDefinition = "createdAt DATE DEFAULT CURDATE()", insertable = false, updatable = false)
	private LocalDate createdAt;

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
}
