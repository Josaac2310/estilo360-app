package com.estilo360.estilo360.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa una reseña de un usuario sobre un empleado.
 */
@Entity
@Table(name = "resenas")
public class Resena {

    /** Identificador único de la reseña */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    
    /** Usuario que realiza la reseña */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Empleado al que se dirige la reseña */
    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    /** Puntuación otorgada */
    private int puntuacion;
    
    /** Comentario de la reseña */
    private String comentario;
    
    /** Fecha de la reseña */
    private LocalDate fecha;

    /** Constructor vacío requerido por JPA */
    public Resena() {}

   
    /** Constructor completo */
    public Resena(Usuario usuario, Empleado empleado, int puntuacion, String comentario, LocalDate fecha) {
        this.usuario = usuario;
        this.empleado = empleado;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId_resena() { return idResena; }
    public void setId_resena(Long id_resena) { this.idResena = id_resena; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}