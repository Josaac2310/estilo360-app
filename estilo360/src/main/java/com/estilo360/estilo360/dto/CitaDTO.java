package com.estilo360.estilo360.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.estilo360.estilo360.entidades.Cita}.
 * 
 * Representa la información de una cita para ser enviada o recibida a través de los endpoints REST,
 * incluyendo datos del usuario, empleado, servicio, fecha, hora y estado de la cita.
 * 
 * @version 1.0
 */
public class CitaDTO {

    /** Identificador único de la cita */
    private Long id_cita;

    /** Identificador del usuario asociado a la cita */
    @NotNull
    private Long usuario_id;

    /** Identificador del empleado asociado a la cita */
    @NotNull
    private Long empleado_id;

    /** Identificador del servicio asociado a la cita */
    @NotNull
    private Long servicio_id;

    /** Fecha de la cita en formato dd/MM/yyyy */
    @NotNull
    private String fecha;

    /** Hora de la cita en formato HH:mm */
    @NotNull
    private String hora;

    /** Estado de la cita (pendiente, confirmada, cancelada, etc.) */
    private String estado;

    /** Observaciones adicionales sobre la cita */
    private String observaciones;
    
    /** Nombre del empleado asociado a la cita (para mostrar en DTO) */
    private String nombreEmpleado;

    /** Nombre del servicio asociado a la cita (para mostrar en DTO) */
    private String nombreServicio;

    /**
     * Constructor vacío para uso de frameworks y serialización.
     */
    public CitaDTO() {}

    /**
     * Constructor completo para inicializar una cita con todos sus datos principales.
     * 
     * @param id_cita Identificador de la cita
     * @param usuario_id Identificador del usuario
     * @param empleado_id Identificador del empleado
     * @param servicio_id Identificador del servicio
     * @param fecha Fecha de la cita
     * @param hora Hora de la cita
     * @param estado Estado de la cita
     * @param observaciones Observaciones de la cita
     */
    public CitaDTO(Long id_cita, Long usuario_id, Long empleado_id, Long servicio_id,
            String fecha, String hora, String estado, String observaciones) {
        this.id_cita = id_cita;
        this.usuario_id = usuario_id;
        this.empleado_id = empleado_id;
        this.servicio_id = servicio_id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y setters

    /** @return Identificador de la cita */
    public Long getId_cita() { return id_cita; }

    /** @return Identificador del usuario */
    public Long getUsuario_id() { return usuario_id; }

    /** @return Identificador del empleado */
    public Long getEmpleado_id() { return empleado_id; }

    /** @return Identificador del servicio */
    public Long getServicio_id() { return servicio_id; }

    /** @return Fecha de la cita */
    public String getFecha() { return fecha; }

    /** @return Hora de la cita */
    public String getHora() { return hora; }

    /** @return Estado de la cita */
    public String getEstado() { return estado; }

    /** @return Observaciones de la cita */
    public String getObservaciones() { return observaciones; }

    /** @return Nombre del empleado asociado */
    public String getNombreEmpleado() { return nombreEmpleado; }

    /** @return Nombre del servicio asociado */
    public String getNombreServicio() { return nombreServicio; }

    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public void setUsuario_id(Long usuario_id) { this.usuario_id = usuario_id; }

    public void setEmpleado_id(Long empleado_id) { this.empleado_id = empleado_id; }

    public void setServicio_id(Long servicio_id) { this.servicio_id = servicio_id; }

    public void setFecha(String fecha) { this.fecha = fecha; }

    public void setHora(String hora) { this.hora = hora; }

    public void setEstado(String estado) { this.estado = estado; }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

