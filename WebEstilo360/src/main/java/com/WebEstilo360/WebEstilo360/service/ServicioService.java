package com.WebEstilo360.WebEstilo360.service;

import com.WebEstilo360.WebEstilo360.dto.ServicioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Servicio para la gestión de servicios ofrecidos.
 * Proporciona métodos para listar, crear, actualizar y eliminar servicios,
 * así como descargar el catálogo de servicios en formato PDF.
 * Interactúa con la API externa mediante RestTemplate.
 * 
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServicioService {

    /** Cliente HTTP para realizar solicitudes REST */
    private final RestTemplate restTemplate;

    /** URL base de la API, inyectada desde configuración */
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Lista todos los servicios disponibles.
     * 
     * @return Lista de ServicioDTO o lista vacía en caso de error
     */
    public List<ServicioDTO> listarServicios() {
        try {
            String url = apiBaseUrl + "/servicios";
            
            ResponseEntity<List<ServicioDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ServicioDTO>>() {}
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al listar servicios: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Obtiene un servicio por su ID.
     * 
     * @param id ID del servicio
     * @return ServicioDTO correspondiente o null si no se encuentra
     */
    public ServicioDTO obtenerServicioPorId(Long id) {
        try {
            String url = apiBaseUrl + "/servicios/" + id;
            
            ResponseEntity<ServicioDTO> response = restTemplate.getForEntity(url, ServicioDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al obtener servicio: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un nuevo servicio.
     * 
     * @param servicioDTO Datos del servicio a crear
     * @return ServicioDTO creado
     * @throws RuntimeException Si ocurre un error al crear el servicio
     */
    public ServicioDTO crearServicio(ServicioDTO servicioDTO) {
        try {
            String url = apiBaseUrl + "/servicios";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ServicioDTO> request = new HttpEntity<>(servicioDTO, headers);
            
            ResponseEntity<ServicioDTO> response = restTemplate.postForEntity(url, request, ServicioDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al crear servicio: " + e.getMessage());
            throw new RuntimeException("Error al crear el servicio");
        }
    }

    /**
     * Actualiza un servicio existente.
     * 
     * @param id ID del servicio a actualizar
     * @param servicioDTO Datos actualizados del servicio
     * @throws RuntimeException Si ocurre un error al actualizar el servicio
     */
    public void actualizarServicio(Long id, ServicioDTO servicioDTO) {
        try {
            String url = apiBaseUrl + "/servicios/" + id;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<ServicioDTO> request = new HttpEntity<>(servicioDTO, headers);
            
            restTemplate.put(url, request);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar servicio: " + e.getMessage());
            throw new RuntimeException("Error al actualizar el servicio");
        }
    }

    /**
     * Elimina un servicio por su ID.
     * 
     * @param id ID del servicio a eliminar
     * @throws RuntimeException Si ocurre un error al eliminar el servicio
     */
    public void eliminarServicio(Long id) {
        try {
            String url = apiBaseUrl + "/servicios/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar servicio: " + e.getMessage());
            throw new RuntimeException("Error al eliminar el servicio");
        }
    }
    
    /**
     * Descarga el catálogo de servicios en formato PDF.
     * 
     * @return Array de bytes con el contenido del PDF
     * @throws RuntimeException Si ocurre un error al descargar el PDF
     */
    public byte[] descargarPdfServicios() {
        try {
            String url = apiBaseUrl + "/servicios/descargar-pdf";

            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error al descargar PDF: " + e.getMessage());
            throw new RuntimeException("Error al descargar el catálogo");
        }
    }
}