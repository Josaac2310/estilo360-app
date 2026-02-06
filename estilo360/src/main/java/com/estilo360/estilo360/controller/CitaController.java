package com.estilo360.estilo360.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estilo360.estilo360.dto.CitaDTO;
import com.estilo360.estilo360.entidades.Cita;
import com.estilo360.estilo360.services.CitaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


/**
 * Controlador REST para la gestión de citas.
 * 
 * Expone endpoints para crear, consultar, actualizar y eliminar citas,
 * así como para obtener información específica del usuario autenticado
 * como próximas citas, historial y horarios disponibles.
 * 
 */
@RestController
@RequestMapping("/citas")
public class CitaController {

	
    /** Servicio de negocio encargado de la gestión de citas */
    private final CitaService citaService;

    
    /**
     * Constructor del controlador de citas.
     * 
     * @param citaService Servicio que contiene la lógica de negocio de citas
     */
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    
    /**
     * Crea una nueva cita.
     * 
     * @param dto Objeto con la información de la cita a crear
     * @return Cita creada
     */
    @PostMapping
    public CitaDTO crear(@Valid @RequestBody CitaDTO dto) {
        return citaService.crearCita(dto);
    }

    
    /**
     * Lista todas las citas registradas.
     * 
     * @return Lista de citas
     */
    @GetMapping
    public List<CitaDTO> listar() {
        return citaService.listarCitas();
    }

    
    /**
     * Obtiene una cita por su identificador.
     * 
     * @param id Identificador único de la cita
     * @return Cita correspondiente al identificador
     */
    @GetMapping("/{id}")
    public CitaDTO obtener(@PathVariable Long id) {
        return citaService.obtenerPorId(id);
    }

    
    /**
     * Elimina una cita por su identificador.
     * 
     * @param id Identificador único de la cita a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        citaService.eliminarCita(id);
    }
    
    
    /**
     * Actualiza la información de una cita existente.
     * 
     * @param id Identificador único de la cita
     * @param dto Objeto con los nuevos datos de la cita
     * @return Cita actualizada
     */
    @PutMapping("/{id}")
    public CitaDTO actualizar(@PathVariable Long id, @Valid @RequestBody CitaDTO dto) {
        return citaService.actualizarCita(id, dto);
    }
    
    
    /**
     * Obtiene la próxima cita del usuario autenticado.
     * 
     * @param request Petición HTTP que contiene el token de autenticación
     * @return Próxima cita del usuario o respuesta sin contenido si no existe
     */
    @GetMapping("/proxima")
    public ResponseEntity<CitaDTO> obtenerProximaCita(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        CitaDTO proxima = citaService.obtenerProximaCita(token);
        
        if (proxima == null) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(proxima);
    }

    
    /**
     * Obtiene el historial de citas del usuario autenticado.
     * 
     * @param request Petición HTTP que contiene el token de autenticación
     * @return Lista de citas del historial del usuario
     */
    @GetMapping("/historial")
    public ResponseEntity<List<CitaDTO>> obtenerHistorial(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(citaService.obtenerHistorialUsuario(token, 3));
    }

    
    /**
     * Obtiene todas las citas asociadas al usuario autenticado.
     * 
     * @param request Petición HTTP que contiene el token de autenticación
     * @return Lista de citas del usuario
     */
    @GetMapping("/mis-citas")
    public ResponseEntity<List<CitaDTO>> obtenerMisCitas(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(citaService.obtenerCitasUsuario(token));
    }
    
    /**
     * Obtiene los horarios disponibles para un empleado en una fecha determinada.
     * 
     * @param empleadoId Identificador del empleado
     * @param fecha Fecha para consultar disponibilidad
     * @return Lista de horarios disponibles
     */
    @GetMapping("/horarios-disponibles")
    public List<String> obtenerHorariosDisponibles(
            @RequestParam Long empleadoId,
            @RequestParam String fecha) {
        return citaService.obtenerHorariosDisponibles(empleadoId, fecha);
    }
    
    
}
