package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Call;

@Repository
public interface CallsRepo extends JpaRepository<Call, Integer> {

	@Query("SELECT c FROM Call c WHERE (c.fkChat.id = :chatId)")
    List<Call> getCallsByFkChat(@Param("chatId") int chatId);
	
	@Query("SELECT c FROM Call c WHERE ((c.fkChat.fkFriendship.fkUser1.id = :userId OR c.fkChat.fkFriendship.fkUser2.id = :userId) AND c.endTime is NULL)")
    Call getCallOfUser(@Param("userId") int userId);
	
	@Query("SELECT COUNT(c) > 0 FROM Call c WHERE ((c.fkChat.fkFriendship.fkUser1.id = :userId OR c.fkChat.fkFriendship.fkUser2.id = :userId) AND c.endTime is NULL)")
    boolean hasCallsOpen(@Param("userId") int userId);
}
