package it.edu.maxplanck.gpoProject_Server_WS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GpoProjectServerWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpoProjectServerWsApplication.class, args);
		
		System.out.println(""
				+ "\n---------------------------"
				+ "\n   SPRING has Started..."
				+ "\n---------------------------\n"
		);
	}
}
