package com.estilo360.estilo360.dto;

/**
 * Data Transfer Object (DTO) para la respuesta de inicio de sesión.
 * 
 * Contiene el token JWT generado tras la autenticación exitosa de un usuario,
 * que será utilizado para acceder a los endpoints protegidos del sistema.
 * 
 * Este DTO se devuelve al cliente después de un login exitoso.
 * 
 * @version 1.0
 */
public class LoginResponseDTO {

    /** Token JWT generado tras la autenticación */
    private String token;

    /**
     * Constructor que inicializa el DTO con el token.
     * 
     * @param token Token JWT del usuario autenticado
     */
    public LoginResponseDTO(String token) {
        this.token = token;
    }

    /** @return Token JWT del usuario */
    public String getToken() {
        return token;
    }
}