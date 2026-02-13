package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.User;

/**
 * Interfaccia che rappresenta le query da fare nel database delle community
 */
@Repository
public interface CommunitiesRepo extends JpaRepository<Community, Integer> {
	
	List<Community> findCommunityByFkUserOwner(User u);
	
    Community getCommunityByInviteCode(String inviteCode);
    
    boolean existsCommunityByInviteCode(String inviteCode);
}