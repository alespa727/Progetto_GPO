package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Attached;

/**
 * Interfaccia che rappresenta le query da fare nel database degli allegati
 */
@Repository
public interface AttachmentsRepo extends JpaRepository<Attached, Integer> {
	
	@Query("SELECT a FROM Attached a WHERE (a.fkMessage.id = :messageId)")
    List<Attached> findByFkMessage(@Param("messageId") Integer messageId);
}