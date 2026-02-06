package com.estilo360.estilo360.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.estilo360.estilo360.entidades.Empleado;

import java.util.Optional;

/**
 * Repositorio JPA para la gestión de entidades {@link Empleado}.
 * 
 * Proporciona métodos de acceso a datos relacionados con los empleados,
 * incluyendo la búsqueda por correo electrónico.
 * 
 * @version 1.0
 */
@Repository
public interface EmpleadoDAO extends JpaRepository<Empleado, Long> {

    /**
     * Busca un empleado por su correo electrónico.
     * 
     * @param correo Correo electrónico del empleado
     * @return Optional que contiene el empleado si existe, o vacío si no se encuentra
     */
    Optional<Empleado> findByCorreo(String correo);
}