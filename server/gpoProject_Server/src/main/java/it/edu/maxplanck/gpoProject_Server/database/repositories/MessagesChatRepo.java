package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.MessageChat;

@Repository
public interface MessagesChatRepo extends JpaRepository<MessageChat, Integer> {

    List<MessageChat> getMessagesByFkChat(int chatId);
    
    @Query(value = "SELECT * FROM messagesChat WHERE (fkChat = :chatId AND id > :lastMessageId) ORDER BY id ASC LIMIT :limit", nativeQuery = true)
    List<MessageChat> findByFkChatIDAndIDGreaterThanOrderByPkIDAsc(@Param("chatId") Integer chatId, @Param("lastMessageId") Integer lastMessageId, @Param("limit") Integer limit);
}