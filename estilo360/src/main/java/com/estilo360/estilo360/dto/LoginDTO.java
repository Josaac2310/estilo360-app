package com.estilo360.estilo360.dto;

/**
 * Data Transfer Object (DTO) para la autenticación de usuarios.
 * 
 * Representa los datos necesarios para iniciar sesión en el sistema,
 * incluyendo el correo electrónico y la contraseña del usuario.
 * 
 * Se utiliza principalmente en los endpoints de login y validación de credenciales.
 * 
 * @version 1.0
 */
public class LoginDTO {

    /** Correo electrónico del usuario */
    private String correo;

    /** Contraseña del usuario */
    private String contrasena;

    // Getters y setters

    /** @return Correo electrónico del usuario */
    public String getCorreo() { return correo; }

    /** @param correo Establece el correo electrónico del usuario */
    public void setCorreo(String correo) { this.correo = correo; }

    /** @return Contraseña del usuario */
    public String getContrasena() { return contrasena; }

    /** @param contrasena Establece la contraseña del usuario */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
