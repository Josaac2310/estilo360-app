package com.estilo360.estilo360.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) para la solicitud de inicio de sesión.
 * 
 * Contiene los datos requeridos para autenticar a un usuario en el sistema,
 * incluyendo el correo electrónico y la contraseña. Se valida que ambos campos
 * no estén vacíos mediante la anotación {@link NotBlank}.
 * 
 * Se utiliza en los endpoints de autenticación para recibir credenciales.
 * 
 * @version 1.0
 */
public class LoginRequestDTO {

    /** Correo electrónico del usuario (no puede estar vacío) */
    @NotBlank
    private String correo;

    /** Contraseña del usuario (no puede estar vacía) */
    @NotBlank
    private String contrasena;

    /** @return Correo electrónico del usuario */
    public String getCorreo() {
        return correo;
    }

    /** @param correo Establece el correo electrónico del usuario */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** @return Contraseña del usuario */
    public String getContrasena() {
        return contrasena;
    }

    /** @param contrasena Establece la contraseña del usuario */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}

