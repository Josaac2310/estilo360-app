package com.estilo360.estilo360.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estilo360.estilo360.entidades.Cita;

/**
 * Repositorio JPA para la gestión de entidades {@link Cita}.
 * 
 * Proporciona métodos de acceso a datos relacionados con las citas,
 * incluyendo búsquedas ordenadas y filtradas por empleado y fecha.
 * 
 */
@Repository
public interface CitaDAO extends JpaRepository<Cita, Long> {
    
    /**
     * Obtiene todas las citas ordenadas por fecha descendente y hora descendente.
     * 
     * @return Lista de citas ordenadas de la más reciente a la más antigua
     */
    List<Cita> findAllByOrderByFechaDescHoraDesc();
    
    /**
     * Obtiene todas las citas ordenadas por fecha ascendente y hora ascendente.
     * 
     * @return Lista de citas ordenadas de la más antigua a la más reciente
     */
    List<Cita> findAllByOrderByFechaAscHoraAsc();
    
    /**
     * Obtiene las citas asociadas a un empleado en una fecha específica.
     * 
     * @param empleadoId Identificador del empleado
     * @param fecha Fecha de la cita
     * @return Lista de citas del empleado en la fecha indicada
     */
    List<Cita> findByEmpleado_IdEmpleadoAndFecha(Long empleadoId, LocalDate fecha);

}