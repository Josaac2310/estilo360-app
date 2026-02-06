package com.WebEstilo360.WebEstilo360.service;

import com.WebEstilo360.WebEstilo360.dto.CitaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para la gestión de citas.
 * Proporciona métodos para listar, crear, actualizar, eliminar y consultar citas,
 * así como obtener la próxima cita, historial y horarios disponibles.
 * Interactúa con la API externa mediante RestTemplate.
 * 
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CitaService {

    /** Cliente HTTP para realizar solicitudes REST */
    private final RestTemplate restTemplate;

    /** URL base de la API, inyectada desde configuración */
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Lista todas las citas existentes.
     * 
     * @return Lista de CitaDTO o lista vacía en caso de error
     */
    public List<CitaDTO> listarCitas() {
        try {
            String url = apiBaseUrl + "/citas";
            
            ResponseEntity<List<CitaDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CitaDTO>>() {}
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al listar citas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene una cita por su identificador.
     * 
     * @param id ID de la cita
     * @return CitaDTO correspondiente o null si ocurre un error
     */
    public CitaDTO obtenerCitaPorId(Long id) {
        try {
            String url = apiBaseUrl + "/citas/" + id;
            
            ResponseEntity<CitaDTO> response = restTemplate.getForEntity(url, CitaDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al obtener cita: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea una nueva cita.
     * 
     * @param citaDTO Datos de la cita a crear
     * @return CitaDTO creada
     * @throws RuntimeException Si ocurre un error al crear la cita
     */
    public CitaDTO crearCita(CitaDTO citaDTO) {
        try {
            String url = apiBaseUrl + "/citas";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<CitaDTO> request = new HttpEntity<>(citaDTO, headers);
            
            ResponseEntity<CitaDTO> response = restTemplate.postForEntity(url, request, CitaDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al crear cita: " + e.getMessage());
            throw new RuntimeException("Error al crear la cita");
        }
    }

    /**
     * Actualiza una cita existente.
     * 
     * @param id ID de la cita a actualizar
     * @param citaDTO Datos actualizados de la cita
     * @throws RuntimeException Si ocurre un error al actualizar la cita
     */
    public void actualizarCita(Long id, CitaDTO citaDTO) {
        try {
            String url = apiBaseUrl + "/citas/" + id;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<CitaDTO> request = new HttpEntity<>(citaDTO, headers);
            
            restTemplate.put(url, request);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
            throw new RuntimeException("Error al actualizar la cita");
        }
    }
    
    /**
     * Elimina una cita por su ID.
     * 
     * @param id ID de la cita a eliminar
     * @throws RuntimeException Si ocurre un error al eliminar la cita
     */
    public void eliminarCita(Long id) {
        try {
            String url = apiBaseUrl + "/citas/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
            throw new RuntimeException("Error al eliminar la cita");
        }
    }
    
    /**
     * Elimina una cita usando token de usuario (cancelación).
     * 
     * @param citaId ID de la cita a cancelar
     * @param token Token de autorización del usuario
     * @throws RuntimeException Si ocurre un error al cancelar la cita
     */
    public void eliminarCita(Long citaId, String token) {
        try {
            String url = apiBaseUrl + "/citas/" + citaId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Void.class
            );

        } catch (Exception e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
            throw new RuntimeException("Error al cancelar la cita");
        }
    }
    
    /**
     * Obtiene la próxima cita del usuario autenticado.
     * 
     * @param token Token de autorización del usuario
     * @return CitaDTO de la próxima cita o null si no hay cita
     */
    public CitaDTO obtenerProximaCita(String token) {
        try {
            String url = apiBaseUrl + "/citas/proxima";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<CitaDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                CitaDTO.class
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error al obtener próxima cita: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene el historial de citas del usuario (últimas 3 citas).
     * 
     * @param token Token de autorización del usuario
     * @return Lista de CitaDTO o lista vacía si ocurre un error
     */
    public List<CitaDTO> obtenerHistorial(String token) {
        try {
            String url = apiBaseUrl + "/citas/historial";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<List<CitaDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<CitaDTO>>() {}
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene todas las citas del usuario autenticado.
     * 
     * @param token Token de autorización del usuario
     * @return Lista de CitaDTO o lista vacía si ocurre un error
     */
    public List<CitaDTO> obtenerMisCitas(String token) {
        try {
            String url = apiBaseUrl + "/citas/mis-citas";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<List<CitaDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<CitaDTO>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (Exception e) {
            System.err.println("Error al obtener mis citas: " + e.getMessage());
            return Collections.emptyList();
        }
    }
 
    /**
     * Obtiene los horarios disponibles para un empleado en una fecha específica.
     * 
     * @param empleadoId ID del empleado
     * @param fecha Fecha en formato YYYY-MM-DD
     * @return Lista de horarios disponibles o lista vacía si ocurre un error
     */
    public List<String> obtenerHorariosDisponibles(Long empleadoId, String fecha) {
        try {
            String url = apiBaseUrl + "/citas/horarios-disponibles?empleadoId=" + empleadoId + "&fecha=" + fecha;
            
            ResponseEntity<List<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al obtener horarios disponibles: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}