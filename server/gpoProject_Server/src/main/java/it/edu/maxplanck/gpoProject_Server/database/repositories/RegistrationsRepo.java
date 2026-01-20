package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Community;
import it.edu.maxplanck.gpoProject_Server.database.model.Registration;
import it.edu.maxplanck.gpoProject_Server.database.model.RegistrationID;

@Repository
public interface RegistrationsRepo extends JpaRepository<Registration, RegistrationID> {

	void registerUserToCommunity(int userId, int communityId);

    List<Community> getUserCommunities(int userId);

    boolean isUserRegisteredToCommunity(int userId, int communityId);
}