package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {

    @Bean
    public CookieManager cookieManager() {
        return new CookieManager();
    }
}