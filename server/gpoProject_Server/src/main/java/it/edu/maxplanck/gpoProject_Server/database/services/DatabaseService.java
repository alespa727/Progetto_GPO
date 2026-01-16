package it.edu.maxplanck.gpoProject_Server.database.services;

import org.springframework.stereotype.Service;

import it.edu.maxplanck.gpoProject_Server.database.repositories.DatabaseRepositories;

@Service
public abstract class DatabaseService {

	private final DatabaseRepositories dbRepositories;
	
	public DatabaseService(DatabaseRepositories dbRepositories) {
		this.dbRepositories = dbRepositories;
	}

	public DatabaseRepositories getDbRepositories() {
		return dbRepositories;
	}
}
