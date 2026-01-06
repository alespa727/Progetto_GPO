package it.edu.maxplanck.gpoProject_Server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.edu.maxplanck.gpoProject_Server.database.modelDB.Community;

@RepositoryRestResource
public interface CommunityRepository extends JpaRepository<Community, Integer> {}
