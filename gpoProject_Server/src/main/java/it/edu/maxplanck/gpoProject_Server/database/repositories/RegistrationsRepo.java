package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Registration;
import it.edu.maxplanck.gpoProject_Server.database.model.RegistrationID;
import it.edu.maxplanck.gpoProject_Server.database.model.User;

/**
 * Interfaccia che rappresenta le query da fare nel database dell'iscrizione alle community
 */
@Repository
public interface RegistrationsRepo extends JpaRepository<Registration, RegistrationID> {

	@Query("SELECT c FROM Registration r JOIN Community c ON (c.id = r.fkCommunity.id AND r.fkUser.id = :userId)")
	List<Community> findCommunitiesOfUser(@Param("userId") Integer userId);
	
	@Query("SELECT u FROM Registration r JOIN User u ON (u.id = r.fkUser.id AND r.fkCommunity.id = :communityId)")
	List<User> findUsersOfCommunty(@Param("communityId") Integer communityId);
	
	boolean existsRegistrationByFkUserAndFkCommunity(User u, Community c);

	@Query("SELECT r FROM Registration r WHERE (r.fkCommunity = :community AND r.fkUser = :user)")
	Registration findByFkCommunityAndFkUser(@Param("community") Community c, @Param("user") User u);
}