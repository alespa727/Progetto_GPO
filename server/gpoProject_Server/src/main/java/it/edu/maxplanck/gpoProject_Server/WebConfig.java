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
            "http://localhost:5173",
            "https://progettogpo.vercel.app",
            "https://weightlessly-tres-dagmar.ngrok-free.dev"
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
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
<<<<<<< HEAD
                .allowedHeaders("*")
                ;
    }
}
