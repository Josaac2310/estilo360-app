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

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

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

    public void eliminarServicio(Long id) {
        try {
            String url = apiBaseUrl + "/servicios/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar servicio: " + e.getMessage());
            throw new RuntimeException("Error al eliminar el servicio");
        }
    }
}
