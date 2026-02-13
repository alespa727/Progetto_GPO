package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Section;

/**
 * Interfaccia che rappresenta le query da fare nel database delle sezioni
 */
@Repository
public interface SectionsRepo extends JpaRepository<Section, Integer> {
	
    List<Section> findSectionsByFkCommunity(Community community);
    
    boolean existsSectionByPkIDAndFkCommunity(Integer id, Community c);
}