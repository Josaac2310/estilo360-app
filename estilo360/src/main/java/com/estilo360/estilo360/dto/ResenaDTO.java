package com.estilo360.estilo360.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.estilo360.estilo360.entidades.Resena}.
 * 
 * Representa la información de una reseña realizada por un usuario sobre un empleado,
 * incluyendo puntuación, comentario y fecha. Se utiliza para enviar y recibir datos
 * a través de los endpoints REST relacionados con reseñas.
 * 
 * Validaciones:
 * - {@code usuarioId} y {@code empleadoId} no pueden ser nulos.
 * - {@code puntuacion} debe estar entre 1 y 5.
 * 
 * @version 1.0
 */
public class ResenaDTO {

    /** Identificador único de la reseña */
    private Long id_resena;

    /** Identificador del usuario que realiza la reseña */
    @NotNull
    private Long usuarioId;

    /** Identificador del empleado al que se le realiza la reseña */
    @NotNull
    private Long empleadoId;

    /** Puntuación otorgada en la reseña (1 a 5) */
    @Min(1)
    @Max(5)
    private int puntuacion;

    /** Comentario adicional de la reseña */
    private String comentario;

    /** Fecha en la que se realizó la reseña, con formato dd-MM-yyyy */
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;

    /**
     * Constructor vacío para uso de frameworks y serialización.
     */
    public ResenaDTO() {}

    /**
     * Constructor completo para inicializar una reseña con todos sus datos.
     * 
     * @param id_resena Identificador de la reseña
     * @param usuarioId Identificador del usuario
     * @param empleadoId Identificador del empleado
     * @param puntuacion Puntuación de la reseña
     * @param comentario Comentario de la reseña
     * @param fecha Fecha de la reseña
     */
    public ResenaDTO(Long id_resena, Long usuarioId, Long empleadoId, int puntuacion, String comentario, LocalDate fecha) {
        this.id_resena = id_resena;
        this.usuarioId = usuarioId;
        this.empleadoId = empleadoId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    // Getters y Setters

    /** @return Identificador de la reseña */
    public Long getId_resena() { return id_resena; }

    /** @param id_resena Establece el identificador de la reseña */
    public void setId_resena(Long id_resena) { this.id_resena = id_resena; }

    /** @return Identificador del usuario que realizó la reseña */
    public Long getUsuarioId() { return usuarioId; }

    /** @param usuarioId Establece el identificador del usuario */
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    /** @return Identificador del empleado reseñado */
    public Long getEmpleadoId() { return empleadoId; }

    /** @param empleadoId Establece el identificador del empleado */
    public void setEmpleadoId(Long empleadoId) { this.empleadoId = empleadoId; }

    /** @return Puntuación de la reseña */
    public int getPuntuacion() { return puntuacion; }

    /** @param puntuacion Establece la puntuación de la reseña */
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    /** @return Comentario de la reseña */
    public String getComentario() { return comentario; }

    /** @param comentario Establece el comentario de la reseña */
    public void setComentario(String comentario) { this.comentario = comentario; }

    /** @return Fecha de la reseña */
    public LocalDate getFecha() { return fecha; }

    /** @param fecha Establece la fecha de la reseña */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
