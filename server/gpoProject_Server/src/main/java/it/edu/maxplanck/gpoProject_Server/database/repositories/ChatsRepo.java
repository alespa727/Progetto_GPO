package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Chat;

@Repository
public interface ChatsRepo extends JpaRepository<Chat, Integer> {
	
	Chat createChat(int friendshipId);

    Chat getChatById(int chatId);

    List<Chat> getChatsByUser(int userId);

    void updateChatLastMessageTime(int chatId);

    void deleteChat(int chatId);
}