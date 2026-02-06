package com.estilo360.estilo360.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración de la aplicación para habilitar la ejecución de métodos asíncronos.
 * 
 * Esta clase permite el uso de la anotación {@code @Async} en métodos del proyecto,
 * facilitando la ejecución de tareas en segundo plano sin bloquear el hilo principal.
 * 
 * @version 1.0
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
