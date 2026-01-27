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
	
	@Query("SELECT c FROM Call c WHERE (c.fkChat.id = :chatId AND c.endTime is NULL)")
    boolean existsCallsByFkChat(@Param("chatId") int chatId);
}
