package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Channel;

/**
 * Interfaccia che rappresenta le query da fare nel database dei canali
 */
@Repository
public interface ChannelsRepo extends JpaRepository<Channel, Integer> {

    List<Channel> getChannelsByFkSection(int sectionId);
}
