package it.edu.maxplanck.gpoProject_Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Gestisce impostazioni di configurazione come il cors
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir.images}")
    private String uploadDirImages;
    
    @Value("${app.upload.dir.files}")
    private String uploadDirFiles;
    
    private final String[] allowedOrigins;

    public WebConfig(@Value("${client.port}") String clientPort) {
        // puoi aggiungere qui più origin se vuoi
        this.allowedOrigins = new String[] {
            clientPort,
            "http://localhost:5173", "http://localhost:4173",
            "https://progettogpo.vercel.app",
            "https://weightlessly-tres-dagmar.ngrok-free.dev",
                "https://ale727.duckdns.org",
                "http://ale727.servegame.com:5173"
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDirImages + "/");
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadDirFiles + "/");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // se usi credenziali, serve
    }
}
