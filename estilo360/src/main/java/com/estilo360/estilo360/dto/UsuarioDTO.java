package com.estilo360.estilo360.dto;


import jakarta.validation.constraints.*;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.estilo360.estilo360.entidades.Usuario}.
 * 
 * Representa la información de un usuario del sistema, incluyendo nombre completo,
 * correo electrónico, número de móvil, rol y contraseña.
 * 
 * Se utiliza tanto para operaciones de creación y actualización de usuarios como
 * para la transferencia de datos entre el backend y los clientes REST.
 * 
 * Validaciones incluidas:
 * - {@code nombre_completo}, {@code rol} y {@code contrasena} no pueden estar vacíos.
 * - {@code correo} debe ser un correo válido y obligatorio.
 * - {@code movil} debe cumplir el patrón de teléfono español.
 * 
 * @version 1.0
 */
public class UsuarioDTO {

    /** Identificador único del usuario */
    private Long id_usuario;
    
    /** Nombre completo del usuario (obligatorio) */
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombre_completo;

    /** Correo electrónico del usuario (obligatorio y válido) */
    @Email(message = "Correo inválido")
    @NotBlank(message = "Correo es obligatorio")
    private String correo;

    /** Número de móvil del usuario (opcional, debe cumplir patrón español) */
    @Pattern(regexp = "^(\\+34|0034|34)?[6789]\\d{8}$", message = "Móvil no válido")
    private String movil;

    /** Rol del usuario (obligatorio) */
    @NotBlank(message = "Rol es obligatorio")
    private String rol;

    /** Contraseña del usuario (obligatoria) */
    @NotBlank(message = "Contraseña es obligatoria")
    private String contrasena;

    /**
     * Constructor vacío para frameworks y serialización.
     */
    public UsuarioDTO() {}

    /**
     * Constructor completo para inicializar un usuario con todos sus datos.
     * 
     * @param id_usuario Identificador del usuario
     * @param nombre_completo Nombre completo del usuario
     * @param correo Correo electrónico del usuario
     * @param movil Número de móvil del usuario
     * @param rol Rol del usuario
     * @param contrasena Contraseña del usuario
     */
    public UsuarioDTO(Long id_usuario, String nombre_completo, String correo, String movil, String rol, String contrasena) {
        this.id_usuario = id_usuario;
        this.nombre_completo = nombre_completo;
        this.correo = correo;
        this.movil = movil;
        this.rol = rol;
        this.contrasena = contrasena;
    }
    
    /** @return Identificador del usuario */
    public Long getId_usuario() { return id_usuario; }

    /** @param id_usuario Establece el identificador del usuario */
    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }

    /** @return Nombre completo del usuario */
    public String getNombre_completo() { return nombre_completo; }

    /** @param nombre_completo Establece el nombre completo del usuario */
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }

    /** @return Correo electrónico del usuario */
    public String getCorreo() { return correo; }

    /** @param correo Establece el correo electrónico del usuario */
    public void setCorreo(String correo) { this.correo = correo; }

    /** @return Número de móvil del usuario */
    public String getMovil() { return movil; }

    /** @param movil Establece el número de móvil del usuario */
    public void setMovil(String movil) { this.movil = movil; }

    /** @return Rol del usuario */
    public String getRol() { return rol; }

    /** @param rol Establece el rol del usuario */
    public void setRol(String rol) { this.rol = rol; }

    /** @return Contraseña del usuario */
    public String getContrasena() { return contrasena; }

    /** @param contrasena Establece la contraseña del usuario */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
