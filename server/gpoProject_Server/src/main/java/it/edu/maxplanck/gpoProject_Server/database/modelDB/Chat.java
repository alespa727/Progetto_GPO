package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Amicizia amicizia;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "last_message")
    private LocalDateTime lastMessage;
}
