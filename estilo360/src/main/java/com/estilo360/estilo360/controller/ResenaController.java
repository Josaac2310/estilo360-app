package com.estilo360.estilo360.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.estilo360.estilo360.dto.ResenaDTO;
import com.estilo360.estilo360.services.ResenaService;
import jakarta.validation.Valid;


/**
 * Controlador REST para la gestión de reseñas.
 * 
 * Expone endpoints para crear, listar y eliminar reseñas,
 * así como para obtener reseñas asociadas a empleados o usuarios.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/reseñas")
public class ResenaController {

    /** Servicio de negocio encargado de la gestión de reseñas */
    private final ResenaService resenaService;

    /**
     * Constructor del controlador de reseñas.
     * 
     * @param resenaService Servicio que contiene la lógica de negocio de reseñas
     */
    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    
    /**
     * Crea una nueva reseña.
     * 
     * @param dto Objeto con la información de la reseña a crear
     * @return Reseña creada
     */
    @PostMapping
    public ResenaDTO crearResena(@Valid @RequestBody ResenaDTO dto) {
        return resenaService.crearResena(dto);
    }

    
    /**
     * Lista todas las reseñas registradas.
     * 
     * @return Lista de todas las reseñas
     */
    @GetMapping
    public List<ResenaDTO> listarTodas() {
        return resenaService.obtenerTodas();
    }

    
    /**
     * Lista las reseñas asociadas a un empleado específico.
     * 
     * @param id Identificador único del empleado
     * @return Lista de reseñas del empleado
     */
    @GetMapping("/empleado/{id}")
    public List<ResenaDTO> listarPorEmpleado(@PathVariable Long id) {
        return resenaService.obtenerPorEmpleado(id);
    }

    
    /**
     * Elimina una reseña por su identificador.
     * 
     * @param id Identificador único de la reseña a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminarResena(@PathVariable Long id) {
        resenaService.eliminarResena(id);
    }
    
    
    /**
     * Lista las reseñas asociadas a un usuario específico.
     * 
     * @param usuarioId Identificador único del usuario
     * @return Lista de reseñas del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public List<ResenaDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return resenaService.obtenerPorUsuario(usuarioId);
    }
}