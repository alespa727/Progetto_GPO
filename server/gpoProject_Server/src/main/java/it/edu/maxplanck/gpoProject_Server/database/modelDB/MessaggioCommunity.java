package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messaggi_community")
public class MessaggioCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "canale_fk")
    private Canale canale;

    @ManyToOne
    @JoinColumn(name = "username_fk")
    private Utente utente;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "sent_at")
    private Instant sentAt;
}
