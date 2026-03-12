package it.edu.maxplanck.gpoProject_Server_WS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir.images}")
    private String uploadDirImages;
    
    @Value("${app.upload.dir.files}")
    private String uploadDirFiles;
    
    private final String[] allowedOrigins;

    public WebConfig(@Value("${server.calls}") String clientPort) {
    	
        // puoi aggiungere qui più origin se vuoi
        this.allowedOrigins = new String[] {
            clientPort
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
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}