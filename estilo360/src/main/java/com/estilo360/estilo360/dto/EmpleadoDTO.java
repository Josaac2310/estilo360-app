package com.estilo360.estilo360.dto;

/**
 * Data Transfer Object (DTO) para la entidad {@link com.estilo360.estilo360.entidades.Empleado}.
 * 
 * Representa la información de un empleado para ser utilizada en la comunicación
 * entre el backend y los clientes a través de endpoints REST.
 * Incluye datos personales y profesionales relevantes del empleado.
 * 
 * @version 1.0
 */
public class EmpleadoDTO {

    /** Identificador único del empleado */
    private Long id_empleado;

    /** Nombre completo del empleado */
    private String nombre_completo;

    /** Especialidad o área de trabajo del empleado */
    private String especialidad;

    /** Correo electrónico del empleado */
    private String correo;

    /** Número de teléfono móvil del empleado */
    private String movil;

    /**
     * Constructor vacío para uso de frameworks y serialización.
     */
    public EmpleadoDTO() {}

    /**
     * Constructor completo para inicializar un empleado con todos sus datos.
     * 
     * @param id_empleado Identificador del empleado
     * @param nombre_completo Nombre completo del empleado
     * @param especialidad Especialidad del empleado
     * @param correo Correo electrónico del empleado
     * @param movil Teléfono móvil del empleado
     */
    public EmpleadoDTO(Long id_empleado, String nombre_completo, String especialidad, String correo, String movil) {
        this.id_empleado = id_empleado;
        this.nombre_completo = nombre_completo;
        this.especialidad = especialidad;
        this.correo = correo;
        this.movil = movil;
    }

    // Getters y Setters

    /** @return Identificador del empleado */
    public Long getId_empleado() { return id_empleado; }

    /** @param id_empleado Establece el identificador del empleado */
    public void setId_empleado(Long id_empleado) { this.id_empleado = id_empleado; }

    /** @return Nombre completo del empleado */
    public String getNombre_completo() { return nombre_completo; }

    /** @param nombre_completo Establece el nombre completo del empleado */
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }

    /** @return Especialidad del empleado */
    public String getEspecialidad() { return especialidad; }

    /** @param especialidad Establece la especialidad del empleado */
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    /** @return Correo electrónico del empleado */
    public String getCorreo() { return correo; }

    /** @param correo Establece el correo electrónico del empleado */
    public void setCorreo(String correo) { this.correo = correo; }

    /** @return Número de teléfono móvil del empleado */
    public String getMovil() { return movil; }

    /** @param movil Establece el número de teléfono móvil del empleado */
    public void setMovil(String movil) { this.movil = movil; }
}