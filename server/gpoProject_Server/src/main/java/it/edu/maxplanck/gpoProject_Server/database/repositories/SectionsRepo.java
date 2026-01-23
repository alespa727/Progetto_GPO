package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Section;

@Repository
public interface SectionsRepo extends JpaRepository<Section, Integer> {

    List<Section> getSectionsByFkCommunity(int communityId);
}