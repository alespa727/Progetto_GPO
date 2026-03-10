package it.edu.maxplanck.gpoProject_Server_WS.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.Section;

@Repository
public interface SectionsRepo extends JpaRepository<Section, Integer> {

}
