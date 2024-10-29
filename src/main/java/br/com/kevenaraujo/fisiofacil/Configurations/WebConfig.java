package br.com.kevenaraujo.fisiofacil.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Restrito ao frontend local
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Incluído OPTIONS
                .allowedHeaders("*") // Permitir todos os cabeçalhos
                .allowCredentials(true);
    }
}
