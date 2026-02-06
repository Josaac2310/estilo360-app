package com.estilo360.estilo360.dto;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.estilo360.estilo360.entidades.Servicio}.
 * 
 * Representa la información de un servicio ofrecido, incluyendo nombre, descripción,
 * precio y duración en minutos. Se utiliza para la comunicación entre el backend
 * y los clientes a través de endpoints REST relacionados con servicios.
 * 
 * Validaciones:
 * - {@code nombre} no puede estar vacío.
 * - {@code precio} y {@code duracion_minutos} deben ser valores positivos.
 * 
 * Se utiliza tanto para la creación como para la actualización de servicios.
 * 
 * @version 1.0
 */
public class ServicioDTO {

    /** Identificador único del servicio */
    private Long id_servicio;

    /** Nombre del servicio (no puede estar vacío) */
    @NotBlank
    private String nombre;

    /** Descripción opcional del servicio */
    private String descripcion;

    /** Precio del servicio (debe ser positivo) */
    @Positive
    private Double precio;

    /** Duración del servicio en minutos (debe ser positivo) */
    @Positive
    private Integer duracion_minutos;

    /**
     * Constructor vacío para uso de frameworks y serialización.
     */
    public ServicioDTO() {}

    /**
     * Constructor completo para inicializar un servicio con todos sus datos.
     * 
     * @param id_servicio Identificador del servicio
     * @param nombre Nombre del servicio
     * @param descripcion Descripción del servicio
     * @param precio Precio del servicio
     * @param duracion_minutos Duración del servicio en minutos
     */
    public ServicioDTO(Long id_servicio, String nombre, String descripcion, Double precio, Integer duracion_minutos) {
        this.id_servicio = id_servicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion_minutos = duracion_minutos;
    }

    // Getters y Setters

    /** @return Identificador del servicio */
    public Long getId_servicio() { return id_servicio; }

    /** @param id_servicio Establece el identificador del servicio */
    public void setId_servicio(Long id_servicio) { this.id_servicio = id_servicio; }

    /** @return Nombre del servicio */
    public String getNombre() { return nombre; }

    /** @param nombre Establece el nombre del servicio */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return Descripción del servicio */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion Establece la descripción del servicio */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return Precio del servicio */
    public Double getPrecio() { return precio; }

    /** @param precio Establece el precio del servicio */
    public void setPrecio(Double precio) { this.precio = precio; }

    /** @return Duración del servicio en minutos */
    public Integer getDuracion_minutos() { return duracion_minutos; }

    /** @param duracion_minutos Establece la duración del servicio en minutos */
    public void setDuracion_minutos(Integer duracion_minutos) { this.duracion_minutos = duracion_minutos; }
}
