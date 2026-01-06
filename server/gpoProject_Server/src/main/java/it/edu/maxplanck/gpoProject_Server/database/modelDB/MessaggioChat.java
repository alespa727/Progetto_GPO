package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messaggi_chat")
public class MessaggioChat {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "chat_fk")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "username_fk")
    private Utente utente;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "sent_at")
    private Instant sentAt;
}
