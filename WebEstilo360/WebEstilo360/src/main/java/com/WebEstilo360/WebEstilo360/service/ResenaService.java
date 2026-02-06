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

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

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

    public void eliminarResena(Long id) {
        try {
            String url = apiBaseUrl + "/reseñas/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar reseña: " + e.getMessage());
            throw new RuntimeException("Error al eliminar la reseña");
        }
    }
}
