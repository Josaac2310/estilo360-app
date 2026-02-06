package com.estilo360.estilo360.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario del sistema.
 * Un usuario puede ser administrador o cliente, tiene datos de contacto,
 * credenciales, verificación de email y puede tener múltiples citas y reseñas.
 * 
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    /** Identificador único del usuario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    
    /** Nombre completo del usuario, obligatorio y con máximo 50 caracteres */
    @Column(length = 50, nullable = false)
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombre_completo;
    
    /** Correo electrónico único y obligatorio, validado */
    @Column(unique = true, nullable = false)
    @Email(message = "Correo inválido")
    @NotBlank(message = "Correo es obligatorio")
    private String correo;
    
    /** Número de móvil del usuario, validado con patrón español */
    @Column(length = 15, nullable = false)
    @Pattern(regexp = "^(\\+34|0034|34)?[6789]\\d{8}$", message = "Móvil no válido")
    private String movil;
    
    /** Rol del usuario, obligatorio (admin o cliente) */
    @Column(nullable = false)
    @NotBlank(message = "Rol es obligatorio")
    private String rol;
    
    /** Contraseña del usuario, obligatoria */
    @Column(nullable = false)
    @NotBlank(message = "Contraseña es obligatoria")
    private String contrasena;
    
    /** Indica si el email del usuario ha sido verificado */
    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = false;

    /** Token utilizado para la verificación del email */
    @Column(name = "token_verificacion", length = 100)
    private String tokenVerificacion;
    
    /** Lista de citas asociadas al usuario */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();
    
    /** Lista de reseñas realizadas por el usuario */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resena> resenas = new ArrayList<>();
    
    /**
     * Constructor por defecto.
     */
    public Usuario() {}
    
    /**
     * Constructor con atributos esenciales del usuario.
     * 
     * @param nombre_completo Nombre completo del usuario
     * @param correo Correo electrónico del usuario
     * @param movil Número de móvil del usuario
     * @param rol Rol del usuario (admin o cliente)
     * @param contrasena Contraseña del usuario
     */
    public Usuario(String nombre_completo, String correo, String movil, String rol, String contrasena) {
        this.nombre_completo = nombre_completo;
        this.correo = correo;
        this.movil = movil;
        this.rol = rol;
        this.contrasena = contrasena;
        this.emailVerificado = false;
    }
    
    /**
     * Obtiene el identificador del usuario.
     * 
     * @return id del usuario
     */
    public Long getId_usuario() { return id_usuario; }
    
    /**
     * Establece el identificador del usuario.
     * 
     * @param id_usuario Nuevo id del usuario
     */
    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }
    
    /**
     * Obtiene el nombre completo del usuario.
     * 
     * @return nombre completo
     */
    public String getNombre_completo() { return nombre_completo; }
    
    /**
     * Establece el nombre completo del usuario.
     * 
     * @param nombre_completo Nuevo nombre completo
     */
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }
    
    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return correo electrónico
     */
    public String getCorreo() { return correo; }
    
    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param correo Nuevo correo electrónico
     */
    public void setCorreo(String correo) { this.correo = correo; }
    
    /**
     * Obtiene el número de móvil del usuario.
     * 
     * @return número de móvil
     */
    public String getMovil() { return movil; }
    
    /**
     * Establece el número de móvil del usuario.
     * 
     * @param movil Nuevo número de móvil
     */
    public void setMovil(String movil) { this.movil = movil; }
    
    /**
     * Obtiene el rol del usuario.
     * 
     * @return rol del usuario
     */
    public String getRol() { return rol; }
    
    /**
     * Establece el rol del usuario.
     * 
     * @param rol Nuevo rol del usuario
     */
    public void setRol(String rol) { this.rol = rol; }
    
    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return contraseña
     */
    public String getContrasena() { return contrasena; }
    
    /**
     * Establece la contraseña del usuario.
     * 
     * @param contrasena Nueva contraseña
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    /**
     * Obtiene la lista de citas asociadas al usuario.
     * 
     * @return lista de citas
     */
    public List<Cita> getCitas() { return citas; }
    
    /**
     * Establece la lista de citas asociadas al usuario.
     * 
     * @param citas Nueva lista de citas
     */
    public void setCitas(List<Cita> citas) { this.citas = citas; }
    
    /**
     * Obtiene la lista de reseñas realizadas por el usuario.
     * 
     * @return lista de reseñas
     */
    public List<Resena> getResenas() { return resenas; }
    
    /**
     * Establece la lista de reseñas realizadas por el usuario.
     * 
     * @param resenas Nueva lista de reseñas
     */
    public void setResenas(List<Resena> resenas) { this.resenas = resenas; }

    /**
     * Indica si el correo electrónico del usuario ha sido verificado.
     * 
     * @return true si el email ha sido verificado, false en caso contrario
     */
    public Boolean getEmailVerificado() { return emailVerificado; }

    /**
     * Establece el estado de verificación del correo electrónico.
     * 
     * @param emailVerificado Nuevo estado de verificación
     */
    public void setEmailVerificado(Boolean emailVerificado) { this.emailVerificado = emailVerificado; }

    /**
     * Obtiene el token utilizado para la verificación del email.
     * 
     * @return token de verificación
     */
    public String getTokenVerificacion() { return tokenVerificacion; }

    /**
     * Establece el token utilizado para la verificación del email.
     * 
     * @param tokenVerificacion Nuevo token de verificación
     */
    public void setTokenVerificacion(String tokenVerificacion) { this.tokenVerificacion = tokenVerificacion; }
}