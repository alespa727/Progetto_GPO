package it.edu.maxplanck.gpoProject_Server_WS.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.CallChat;

public interface CallsChatRepo extends JpaRepository<CallChat, Integer> {

}
