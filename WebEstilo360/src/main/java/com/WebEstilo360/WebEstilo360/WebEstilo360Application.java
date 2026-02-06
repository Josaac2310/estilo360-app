package com.WebEstilo360.WebEstilo360;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.WebEstilo360.WebEstilo360.interceptor.JwtInterceptor;

/**
 * Clase principal de la aplicación WebEstilo360.
 * Configura el contexto de Spring Boot, inicializa la aplicación y define beans necesarios.
 * 
 * Contiene la configuración principal de la aplicación y el bean RestTemplate con un interceptor JWT.
 * 
 * @version 1.0
 */
@Configuration
@SpringBootApplication
public class WebEstilo360Application {

    /** Interceptor para agregar JWT a las solicitudes HTTP */
    private final JwtInterceptor jwtInterceptor;

    /**
     * Constructor de la aplicación que inyecta el interceptor JWT.
     * 
     * @param jwtInterceptor Interceptor que se utiliza para incluir el token JWT en las solicitudes
     */
    public WebEstilo360Application(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    /**
     * Método principal que arranca la aplicación Spring Boot.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(WebEstilo360Application.class, args);
    }

    /**
     * Define un bean de RestTemplate con el interceptor JWT configurado.
     * Permite realizar solicitudes HTTP con el token automáticamente incluido.
     * 
     * @return Instancia de RestTemplate con interceptor JWT
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(jwtInterceptor));
        return restTemplate;
    }
}