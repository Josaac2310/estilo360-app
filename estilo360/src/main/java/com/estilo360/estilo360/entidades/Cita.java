package com.estilo360.estilo360.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad que representa una cita en el sistema.
 * 
 * Cada cita está asociada a un usuario, un empleado y un servicio, y contiene
 * información sobre la fecha, hora, estado y observaciones adicionales.
 * 
 * Estados posibles: "pendiente", "confirmada", "cancelada".
 * 
 * Esta entidad se mapea a la tabla "citas" de la base de datos.
 * 
 * @version 1.0
 */
@Entity
@Table(name = "citas")
public class Cita {

    /** Identificador único de la cita */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cita;

    /** Usuario que solicita la cita */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Empleado asignado a la cita */
    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    /** Servicio que se realizará en la cita */
    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    /** Fecha de la cita */
    @Column(nullable = false)
    @NotNull
    private LocalDate fecha;

    /** Hora de la cita */
    @Column(nullable = false)
    @NotNull
    private LocalTime hora;

    /** Estado de la cita: pendiente, confirmada o cancelada */
    @Column(nullable = false)
    private String estado;

    /** Observaciones adicionales sobre la cita */
    @Column(length = 255)
    private String observaciones;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Cita() {}

    /**
     * Constructor completo para crear una nueva cita con todos sus datos.
     * 
     * @param usuario Usuario que solicita la cita
     * @param empleado Empleado asignado a la cita
     * @param servicio Servicio que se realizará
     * @param fecha Fecha de la cita
     * @param hora Hora de la cita
     * @param estado Estado de la cita
     * @param observaciones Observaciones adicionales
     */
    public Cita(Usuario usuario, Empleado empleado, Servicio servicio,
                LocalDate fecha, LocalTime hora, String estado, String observaciones) {
        this.usuario = usuario;
        this.empleado = empleado;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    /** @return Identificador de la cita */
    public Long getId_cita() { return id_cita; }

    /** @return Usuario que solicitó la cita */
    public Usuario getUsuario() { return usuario; }

    /** @return Empleado asignado a la cita */
    public Empleado getEmpleado() { return empleado; }

    /** @return Servicio asociado a la cita */
    public Servicio getServicio() { return servicio; }

    /** @return Fecha de la cita */
    public LocalDate getFecha() { return fecha; }

    /** @return Hora de la cita */
    public LocalTime getHora() { return hora; }

    /** @return Estado de la cita */
    public String getEstado() { return estado; }

    /** @return Observaciones de la cita */
    public String getObservaciones() { return observaciones; }

    /** @param usuario Establece el usuario de la cita */
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    /** @param empleado Establece el empleado de la cita */
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    /** @param servicio Establece el servicio de la cita */
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    /** @param fecha Establece la fecha de la cita */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    /** @param hora Establece la hora de la cita */
    public void setHora(LocalTime hora) { this.hora = hora; }

    /** @param estado Establece el estado de la cita */
    public void setEstado(String estado) { this.estado = estado; }

    /** @param observaciones Establece las observaciones de la cita */
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
