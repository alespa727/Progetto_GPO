package it.edu.maxplanck.gpoProject_Server_WS.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.CallChat;

@Repository
public interface CallsChatRepo extends JpaRepository<CallChat, Integer> {

}
