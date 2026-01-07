package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "community")
public class Community {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "owner_fk")
    private Utente owner;

    @Column(name = "invite_code", length = 20)
    private String inviteCode;

    @Column(name = "is_code_valid")
    private Boolean isCodeValid;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
