package com.estilo360.estilo360.dto;

/**
 * Data Transfer Object (DTO) que encapsula un token de autenticación.
 * 
 * Se utiliza para transferir tokens JWT o similares entre el servidor y el cliente,
 * generalmente tras un proceso de autenticación o renovación de sesión.
 * 
 * Contiene únicamente el token como cadena de texto.
 * 
 * @version 1.0
 */
public class TokenDTO {

    /** Token de autenticación */
    private String token;

    /**
     * Constructor que inicializa el DTO con un token.
     * 
     * @param token Token de autenticación
     */
    public TokenDTO(String token) { 
        this.token = token; 
    }

    /** @return Token de autenticación */
    public String getToken() { 
        return token; 
    }

    /** @param token Establece el token de autenticación */
    public void setToken(String token) { 
        this.token = token; 
    }
}