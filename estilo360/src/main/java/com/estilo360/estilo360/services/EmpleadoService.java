package com.estilo360.estilo360.services;

import org.springframework.stereotype.Service;
import com.estilo360.estilo360.dao.EmpleadoDAO;
import com.estilo360.estilo360.dto.EmpleadoDTO;
import com.estilo360.estilo360.entidades.Empleado;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para la gestión de empleados.
 * Proporciona métodos para crear, listar, actualizar, obtener y eliminar empleados.
 * 
 * @version 1.0
 */
@Service
public class EmpleadoService {

    /** DAO para acceso a datos de empleados */
    private final EmpleadoDAO empleadoDAO;

    /**
     * Constructor que inyecta el DAO de empleados.
     * 
     * @param empleadoDAO Componente de acceso a datos de empleados
     */
    public EmpleadoService(EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    /**
     * Crea un nuevo empleado en la base de datos.
     * 
     * @param dto DTO con los datos del empleado a crear
     * @return DTO del empleado creado, incluyendo su ID generado
     */
    public EmpleadoDTO crearEmpleado(EmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setNombre_completo(dto.getNombre_completo());
        empleado.setEspecialidad(dto.getEspecialidad());
        empleado.setCorreo(dto.getCorreo());
        empleado.setMovil(dto.getMovil());

        empleado = empleadoDAO.save(empleado);

        return new EmpleadoDTO(
                empleado.getId_empleado(),
                empleado.getNombre_completo(),
                empleado.getEspecialidad(),
                empleado.getCorreo(),
                empleado.getMovil()
        );
    }

    /**
     * Obtiene la lista de todos los empleados registrados.
     * 
     * @return Lista de DTOs de empleados
     */
    public List<EmpleadoDTO> listarEmpleados() {
        return empleadoDAO.findAll()
                .stream()
                .map(e -> new EmpleadoDTO(
                        e.getId_empleado(),
                        e.getNombre_completo(),
                        e.getEspecialidad(),
                        e.getCorreo(),
                        e.getMovil()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un empleado existente.
     * 
     * @param id ID del empleado a actualizar
     * @param dto DTO con los nuevos datos del empleado
     * @return DTO del empleado actualizado
     * @throws RuntimeException si el empleado no existe
     */
    public EmpleadoDTO actualizarEmpleado(Long id, EmpleadoDTO dto) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        empleado.setNombre_completo(dto.getNombre_completo());
        empleado.setEspecialidad(dto.getEspecialidad());
        empleado.setCorreo(dto.getCorreo());
        empleado.setMovil(dto.getMovil());

        empleado = empleadoDAO.save(empleado);

        return new EmpleadoDTO(
                empleado.getId_empleado(),
                empleado.getNombre_completo(),
                empleado.getEspecialidad(),
                empleado.getCorreo(),
                empleado.getMovil()
        );
    }

    /**
     * Elimina un empleado por su ID.
     * 
     * @param id ID del empleado a eliminar
     * @throws RuntimeException si el empleado no existe
     */
    public void eliminarEmpleado(Long id) {
        if (!empleadoDAO.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado");
        }
        
        empleadoDAO.deleteById(id);
    }

    /**
     * Obtiene los datos de un empleado por su ID.
     * 
     * @param id ID del empleado a obtener
     * @return DTO del empleado
     * @throws RuntimeException si el empleado no existe
     */
    public EmpleadoDTO obtenerEmpleado(Long id) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        return new EmpleadoDTO(
                empleado.getId_empleado(),
                empleado.getNombre_completo(),
                empleado.getEspecialidad(),
                empleado.getCorreo(),
                empleado.getMovil()
        );
    }
}
