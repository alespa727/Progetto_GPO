package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "iscrizione")
public class Iscrizione {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "community_fk")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "username_fk")
    private Utente utente;

    private LocalDateTime date;
}
