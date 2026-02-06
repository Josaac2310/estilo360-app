package com.estilo360.estilo360.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un servicio ofrecido en el sistema.
 * Cada servicio tiene un nombre, descripción, precio y duración,
 * y puede estar asociado a múltiples citas.
 * 
 * @version 1.0
 */
@Entity
@Table(name = "servicios")
public class Servicio {
    
    /** Identificador único del servicio */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_servicio;
    
    /** Nombre del servicio, obligatorio y con máximo 50 caracteres */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    /** Descripción opcional del servicio, máximo 255 caracteres */
    @Column(length = 255)
    private String descripcion;
    
    /** Precio del servicio, obligatorio y mayor que 0 */
    @Column(nullable = false)
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;
    
    /** Duración del servicio en minutos, obligatorio y mayor que 0 */
    @Column(nullable = false)
    @Positive(message = "La duración debe ser mayor que 0")
    private Integer duracion_minutos;
    
    /** 
     * Lista de citas asociadas a este servicio.
     * Un servicio puede estar en muchas citas.
     */
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();
    
    /**
     * Constructor por defecto.
     */
    public Servicio() {}
    
    /**
     * Constructor con atributos principales del servicio.
     * 
     * @param nombre Nombre del servicio
     * @param descripcion Descripción del servicio
     * @param precio Precio del servicio
     * @param duracion_minutos Duración del servicio en minutos
     */
    public Servicio(String nombre, String descripcion, Double precio, Integer duracion_minutos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion_minutos = duracion_minutos;
    }
    
    /**
     * Obtiene el identificador del servicio.
     * 
     * @return id del servicio
     */
    public Long getId_servicio() { return id_servicio; }
    
    /**
     * Establece el identificador del servicio.
     * 
     * @param id_servicio Nuevo id del servicio
     */
    public void setId_servicio(Long id_servicio) { this.id_servicio = id_servicio; }
    
    /**
     * Obtiene el nombre del servicio.
     * 
     * @return nombre del servicio
     */
    public String getNombre() { return nombre; }
    
    /**
     * Establece el nombre del servicio.
     * 
     * @param nombre Nuevo nombre del servicio
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    /**
     * Obtiene la descripción del servicio.
     * 
     * @return descripción del servicio
     */
    public String getDescripcion() { return descripcion; }
    
    /**
     * Establece la descripción del servicio.
     * 
     * @param descripcion Nueva descripción del servicio
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    /**
     * Obtiene el precio del servicio.
     * 
     * @return precio del servicio
     */
    public Double getPrecio() { return precio; }
    
    /**
     * Establece el precio del servicio.
     * 
     * @param precio Nuevo precio del servicio
     */
    public void setPrecio(Double precio) { this.precio = precio; }
    
    /**
     * Obtiene la duración del servicio en minutos.
     * 
     * @return duración en minutos
     */
    public Integer getDuracion_minutos() { return duracion_minutos; }
    
    /**
     * Establece la duración del servicio en minutos.
     * 
     * @param duracion_minutos Nueva duración en minutos
     */
    public void setDuracion_minutos(Integer duracion_minutos) { this.duracion_minutos = duracion_minutos; }
    
    /**
     * Obtiene la lista de citas asociadas al servicio.
     * 
     * @return lista de citas
     */
    public List<Cita> getCitas() { return citas; }
    
    /**
     * Establece la lista de citas asociadas al servicio.
     * 
     * @param citas Nueva lista de citas
     */
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}