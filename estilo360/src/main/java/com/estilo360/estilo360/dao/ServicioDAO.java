package com.estilo360.estilo360.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estilo360.estilo360.entidades.Servicio;

/**
 * Repositorio JPA para la gestión de entidades {@link Servicio}.
 * 
 * Proporciona métodos de acceso a datos relacionados con los servicios.
 * Al extender JpaRepository, hereda operaciones CRUD básicas.
 * 
 * @version 1.0
 */
@Repository
public interface ServicioDAO extends JpaRepository<Servicio, Long> {

}