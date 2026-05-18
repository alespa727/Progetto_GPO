package it.edu.maxplanck.gpoProject_Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main
 */
@SpringBootApplication
public class Core {
	
	public static void main(String[] args) {
        System.out.println("Utente = " + System.getenv("DB_USERNAME"));
        System.out.println("Password = " + System.getenv("DB_PASSWORD"));
		SpringApplication.run(Core.class, args);
	}
}
