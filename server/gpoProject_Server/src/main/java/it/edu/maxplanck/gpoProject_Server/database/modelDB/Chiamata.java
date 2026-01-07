package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chiamate")
public class Chiamata {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "chat_fk")
    private Chat chat;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_finished")
    private Boolean isFinished;
}
