package it.edu.maxplanck.gpoProject_Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main
 */
@SpringBootApplication
public class Core {
	
	public static void main(String[] args) {
		  System.out.println("JDBC_DATABASE_URL = " + System.getenv("JDBC_DATABASE_URL"));
        System.out.println("JDBC_DATABASE_USERNAME = " + System.getenv("JDBC_DATABASE_USERNAME"));
		SpringApplication.run(Core.class, args);
	}
}
