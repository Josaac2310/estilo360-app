package com.WebEstilo360.WebEstilo360.service;

import com.WebEstilo360.WebEstilo360.dto.EmpleadoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Servicio para la gestión de empleados.
 * Proporciona métodos para listar, crear, actualizar y eliminar empleados,
 * interactuando con la API externa mediante RestTemplate.
 * 
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class EmpleadoService {

    /** Cliente HTTP para realizar solicitudes REST */
    private final RestTemplate restTemplate;

    /** URL base de la API, inyectada desde configuración */
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Lista todos los empleados existentes.
     * 
     * @return Lista de EmpleadoDTO o lista vacía en caso de error
     */
    public List<EmpleadoDTO> listarEmpleados() {
        try {
            String url = apiBaseUrl + "/empleados";
            
            ResponseEntity<List<EmpleadoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmpleadoDTO>>() {}
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Obtiene un empleado por su identificador.
     * 
     * @param id ID del empleado
     * @return EmpleadoDTO correspondiente o null si ocurre un error
     */
    public EmpleadoDTO obtenerEmpleadoPorId(Long id) {
        try {
            String url = apiBaseUrl + "/empleados/" + id;
            
            ResponseEntity<EmpleadoDTO> response = restTemplate.getForEntity(url, EmpleadoDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al obtener empleado: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un nuevo empleado.
     * 
     * @param empleadoDTO Datos del empleado a crear
     * @return EmpleadoDTO creado
     * @throws RuntimeException Si ocurre un error al crear el empleado
     */
    public EmpleadoDTO crearEmpleado(EmpleadoDTO empleadoDTO) {
        try {
            String url = apiBaseUrl + "/empleados";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<EmpleadoDTO> request = new HttpEntity<>(empleadoDTO, headers);
            
            ResponseEntity<EmpleadoDTO> response = restTemplate.postForEntity(url, request, EmpleadoDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al crear empleado: " + e.getMessage());
            throw new RuntimeException("Error al crear el empleado");
        }
    }

    /**
     * Actualiza un empleado existente.
     * 
     * @param id ID del empleado a actualizar
     * @param empleadoDTO Datos actualizados del empleado
     * @throws RuntimeException Si ocurre un error al actualizar el empleado
     */
    public void actualizarEmpleado(Long id, EmpleadoDTO empleadoDTO) {
        try {
            String url = apiBaseUrl + "/empleados/" + id;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<EmpleadoDTO> request = new HttpEntity<>(empleadoDTO, headers);
            
            restTemplate.put(url, request);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            throw new RuntimeException("Error al actualizar el empleado");
        }
    }

    /**
     * Elimina un empleado por su ID.
     * 
     * @param id ID del empleado a eliminar
     * @throws RuntimeException Si ocurre un error al eliminar el empleado
     */
    public void eliminarEmpleado(Long id) {
        try {
            String url = apiBaseUrl + "/empleados/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            throw new RuntimeException("Error al eliminar el empleado");
        }
    }
}