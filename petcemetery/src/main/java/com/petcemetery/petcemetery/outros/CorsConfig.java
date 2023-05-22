package com.petcemetery.petcemetery.outros;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Especifica o padrão de URL ao qual as configurações de CORS se aplicam.
                        .allowedOrigins("http://localhost:3000") // Define os domínios permitidos para fazer solicitações ao backend.
                        .allowedMethods("GET", "POST") // Especifica os métodos HTTP que são permitidos nas solicitações CORS.
                        .allowedHeaders("*"); // Define quais cabeçalhos HTTP são permitidos nas solicitações CORS.
            }
        };
    }
}
