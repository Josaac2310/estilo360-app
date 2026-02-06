package com.estilo360.estilo360.controller;

import org.springframework.web.bind.annotation.*;
import com.estilo360.estilo360.services.EmpleadoService;
import com.estilo360.estilo360.dto.EmpleadoDTO;

import java.util.List;


/**
 * Controlador REST para la gestión de empleados.
 * 
 * Proporciona endpoints para crear, listar, obtener, actualizar
 * y eliminar empleados dentro del sistema.
 * 
 */
@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    
    
    /**
     * Constructor del controlador de empleados.
     * 
     * @param empleadoService Servicio que contiene la lógica de negocio de empleados
     */
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    
    /**
     * Crea un nuevo empleado.
     * 
     * @param dto Objeto con la información del empleado a crear
     * @return Empleado creado
     */
    @PostMapping
    public EmpleadoDTO crearEmpleado(@RequestBody EmpleadoDTO dto) {
        return empleadoService.crearEmpleado(dto);
    }

    
    
    /**
     * Lista todos los empleados registrados.
     * 
     * @return Lista de empleados
     */
    @GetMapping
    public List<EmpleadoDTO> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

    /**
     * Obtiene un empleado por su identificador.
     * 
     * @param id Identificador único del empleado
     * @return Empleado correspondiente al identificador
     */
    @GetMapping("/{id}")
    public EmpleadoDTO obtenerEmpleado(@PathVariable Long id) {
        return empleadoService.obtenerEmpleado(id);
    }

    

    /**
     * Actualiza la información de un empleado existente.
     * 
     * @param id Identificador único del empleado
     * @param dto Objeto con los nuevos datos del empleado
     * @return Empleado actualizado
     */
    @PutMapping("/{id}")
    public EmpleadoDTO actualizarEmpleado(@PathVariable Long id, @RequestBody EmpleadoDTO dto) {
        return empleadoService.actualizarEmpleado(id, dto);
    }

    /**
     * Elimina un empleado por su identificador.
     * 
     * @param id Identificador único del empleado a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
    }
}