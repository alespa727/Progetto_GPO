package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

	@Bean
	public TokenManager tokenManager() {
		return new TokenManager();
	}
}
