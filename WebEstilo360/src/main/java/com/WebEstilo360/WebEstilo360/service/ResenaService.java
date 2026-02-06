package com.WebEstilo360.WebEstilo360.service;

import com.WebEstilo360.WebEstilo360.dto.ResenaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Servicio para la gestión de reseñas.
 * Proporciona métodos para listar, crear y eliminar reseñas,
 * así como obtener reseñas de un usuario específico.
 * Interactúa con la API externa mediante RestTemplate.
 * 
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ResenaService {

    /** Cliente HTTP para realizar solicitudes REST */
    private final RestTemplate restTemplate;

    /** URL base de la API, inyectada desde configuración */
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Lista todas las reseñas disponibles.
     * 
     * @return Lista de ResenaDTO o lista vacía en caso de error
     */
    public List<ResenaDTO> listarResenas() {
        try {
            String url = apiBaseUrl + "/reseñas";
            
            ResponseEntity<List<ResenaDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResenaDTO>>() {}
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al listar reseñas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Elimina una reseña por su ID.
     * 
     * @param id ID de la reseña a eliminar
     * @throws RuntimeException Si ocurre un error al eliminar la reseña
     */
    public void eliminarResena(Long id) {
        try {
            String url = apiBaseUrl + "/reseñas/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar reseña: " + e.getMessage());
            throw new RuntimeException("Error al eliminar la reseña");
        }
    }
    
    /**
     * Crea una nueva reseña.
     * 
     * @param resenaDTO Datos de la reseña a crear
     * @return ResenaDTO creada
     * @throws RuntimeException Si ocurre un error al crear la reseña
     */
    public ResenaDTO crearResena(ResenaDTO resenaDTO) {
        try {
            String url = apiBaseUrl + "/reseñas";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ResenaDTO> request = new HttpEntity<>(resenaDTO, headers);
            
            ResponseEntity<ResenaDTO> response = restTemplate.postForEntity(url, request, ResenaDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al crear reseña: " + e.getMessage());
            throw new RuntimeException("Error al crear la reseña");
        }
    }

    /**
     * Obtiene todas las reseñas de un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de ResenaDTO del usuario o lista vacía en caso de error
     */
    public List<ResenaDTO> obtenerResenasPorUsuario(Long usuarioId) {
        try {
            String url = apiBaseUrl + "/reseñas/usuario/" + usuarioId;
            
            ResponseEntity<List<ResenaDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResenaDTO>>() {}
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas del usuario: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}