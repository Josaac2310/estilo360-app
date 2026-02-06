package com.estilo360.estilo360.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de la aplicación.
 * Define los filtros de JWT, permisos de endpoints, gestión de sesiones
 * y codificación de contraseñas.
 * 
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Filtro de autenticación JWT */
    private final JwtFilter jwtFilter;

    /**
     * Constructor que inyecta el filtro JWT.
     * 
     * @param jwtFilter Filtro de seguridad JWT
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Proporciona un codificador de contraseñas BCrypt para la autenticación.
     * 
     * @return Instancia de BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * Define los endpoints públicos y protegidos, desactiva CSRF,
     * establece política de sesión sin estado y añade el filtro JWT.
     * 
     * @param http Objeto HttpSecurity para configurar la seguridad web
     * @return Configuración completa de la cadena de filtros de seguridad
     * @throws Exception Si ocurre un error al construir la configuración
     */
    @Bean
    @SuppressWarnings("removal")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/usuarios/**").permitAll() // .hasAuthority("admin")
                //.anyRequest().authenticated() // para requerir Auth Token en todos los endpoints
                .anyRequest().permitAll() // permite todos los demás endpoints sin token
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Proporciona el AuthenticationManager de Spring para la autenticación de usuarios.
     * 
     * @param authConfig Configuración de autenticación
     * @return Instancia de AuthenticationManager
     * @throws Exception Si ocurre un error al obtener el AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}