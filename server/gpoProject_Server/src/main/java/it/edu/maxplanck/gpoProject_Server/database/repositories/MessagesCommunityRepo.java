package it.edu.maxplanck.gpoProject_Server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.MessageCommunity;

@Repository
public interface MessagesCommunityRepo extends JpaRepository<MessageCommunity, Integer> {

}