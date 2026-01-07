package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "canali")
public class Canale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sezione_fk")
    private SezioneCanali sezione;

    private String nome;

    private String tipo; // testo | vocale

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "creato_il")
    private LocalDateTime creatoIl;
}

