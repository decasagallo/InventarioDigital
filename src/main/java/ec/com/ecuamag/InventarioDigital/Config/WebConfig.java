package ec.com.ecuamag.InventarioDigital.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite todas las rutas de tu API
                .allowedOrigins("https://inventariodigital.onrender.com","http://localhost:8080") // Agrega tu dominio de frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos HTTP permitidos
                .allowedHeaders("Authorization", "Content-Type"); // Encabezados permitidos // Permite el uso de cookies y credenciales
    }
}
