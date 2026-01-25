package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Chat;
import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;

@Repository
public interface ChatsRepo extends JpaRepository<Chat, Integer> {
	
	@Query("SELECT c FROM Chat c WHERE (c.fkFriendship.fkUser1.id = :userId) OR (c.fkFriendship.fkUser2.id = :userId)")
	List<Chat> findChatByUserId(@Param("userId") Integer userId);
	
    Chat findChatByPkID(Integer chatId);
    
    boolean existsByFkFriendship(Friendship f);
}