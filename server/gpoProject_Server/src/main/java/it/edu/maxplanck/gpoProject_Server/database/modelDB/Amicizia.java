package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "amicizie")
public class Amicizia {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "username_fk1")
    private Utente utente1;

    @ManyToOne
    @JoinColumn(name = "username_fk2")
    private Utente utente2;

    @Column(name = "created_at")
    private Instant createdAt;
}
