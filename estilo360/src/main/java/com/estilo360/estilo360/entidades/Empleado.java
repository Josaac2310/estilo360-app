package com.estilo360.estilo360.entidades;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un empleado en el sistema.
 * 
 * Cada empleado puede atender múltiples citas y recibir varias reseñas.
 */
@Entity
@Table(name = "empleados")
public class Empleado {

    /** Identificador único del empleado */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    /** Nombre completo del empleado */
    @Column(nullable = false, length = 50)
    private String nombre_completo;

    /** Especialidad del empleado */
    @Column(nullable = false, length = 50)
    private String especialidad;

    /** Correo electrónico del empleado (único) */
    @Column(nullable = false, length = 255, unique = true)
    private String correo;

    /** Número de móvil del empleado */
    @Column(nullable = false, length = 15)
    private String movil;

    /** Relación: un empleado atiende muchas citas */
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();

    /** Relación: un empleado puede recibir muchas reseñas */
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resena> resenas = new ArrayList<>();

    /** Constructor vacío requerido por JPA */
    public Empleado() {}

    // Getters y Setters
    public Long getId_empleado() { return idEmpleado; }
    public void setId_empleado(Long id_empleado) { this.idEmpleado = id_empleado; }

    public String getNombre_completo() { return nombre_completo; }
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getMovil() { return movil; }
    public void setMovil(String movil) { this.movil = movil; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }

    public List<Resena> getResenas() { return resenas; }
    public void setResenas(List<Resena> resenas) { this.resenas = resenas; }
}