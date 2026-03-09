package it.edu.maxplanck.gpoProject_Server_WS.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.edu.maxplanck.gpoProject_Server_WS.database.models.Registration;
import it.edu.maxplanck.gpoProject_Server_WS.database.models.RegistrationID;

public interface RegistrationsRepo extends JpaRepository<Registration, RegistrationID> {

}
