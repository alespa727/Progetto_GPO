package it.edu.maxplanck.gpoProject_Server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.edu.maxplanck.gpoProject_Server.database.modelDB.Utente;

@RepositoryRestResource
public interface UtenteRepository extends JpaRepository<Utente, String> {}
