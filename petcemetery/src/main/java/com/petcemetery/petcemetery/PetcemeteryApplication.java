package com.petcemetery.petcemetery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@SpringBootApplication
public class PetcemeteryApplication implements WebMvcConfigurer{

	@Autowired
    private AuthenticationInterceptor authenticationInterceptor;

	public static void main(String[] args) {
		SpringApplication.run(PetcemeteryApplication.class, args);
	}	

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/home", "/outra-rota-restrita/**");
    }
}
