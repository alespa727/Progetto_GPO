package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;

@Repository
public interface MessagesChatRepo extends JpaRepository<MessageChat, Integer> {

    List<MessageChat> getMessagesByFkChat(int chatId);
}