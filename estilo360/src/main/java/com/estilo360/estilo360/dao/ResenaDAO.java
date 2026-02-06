package com.estilo360.estilo360.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.estilo360.estilo360.entidades.Resena;

/**
 * Repositorio JPA para la gestión de entidades {@link Resena}.
 * 
 * Proporciona métodos de acceso a datos relacionados con las reseñas,
 * incluyendo búsquedas por empleado y por usuario.
 * 
 * @version 1.0
 */
//@Repository
public interface ResenaDAO extends JpaRepository<Resena, Long> {
    
    /**
     * Obtiene todas las reseñas asociadas a un empleado específico.
     * 
     * @param empleadoId Identificador único del empleado
     * @return Lista de reseñas del empleado
     */
    List<Resena> findByEmpleado_IdEmpleado(Long empleadoId);
    
    /**
     * Obtiene todas las reseñas asociadas a un usuario específico.
     * 
     * @param usuarioId Identificador único del usuario
     * @return Lista de reseñas del usuario
     */
    @Query("SELECT r FROM Resena r WHERE r.usuario.id_usuario = :usuarioId")
    List<Resena> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}