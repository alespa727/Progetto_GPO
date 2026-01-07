package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "allegati")
public class Allegato {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "messaggio_chat_fk")
    private MessaggioChat messaggioChat;

    private String path;
}
