package ec.com.ecuamag.InventarioDigital.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://inventariodigital.onrender.com",
                        "http://localhost:8080",
                        "http://localhost:63342",
                        "http://127.0.0.1:63342"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type", "X-Auth-Token");
    }
}
