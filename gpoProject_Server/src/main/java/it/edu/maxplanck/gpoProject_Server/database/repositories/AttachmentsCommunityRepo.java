package it.edu.maxplanck.gpoProject_Server.database.repositories;

import it.edu.maxplanck.gpoProject_Server.database.model.AttachedChat;
import it.edu.maxplanck.gpoProject_Server.database.model.AttachedCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaccia che rappresenta le query da fare nel database degli allegati
 */
@Repository
public interface AttachmentsCommunityRepo extends JpaRepository<AttachedCommunity, Integer> {
	
	@Query("SELECT a FROM AttachedCommunity a WHERE (a.fkMessage.pkID = :messageId)")
    List<AttachedCommunity> findByFkMessage(@Param("messageId") Integer messageId);
}