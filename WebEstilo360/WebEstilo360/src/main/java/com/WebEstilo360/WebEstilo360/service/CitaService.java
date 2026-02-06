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

@Service
@RequiredArgsConstructor
public class CitaService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

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
    
    public void eliminarCita(Long id) {
        try {
            String url = apiBaseUrl + "/citas/" + id;
            
            restTemplate.delete(url);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
            throw new RuntimeException("Error al eliminar la cita");
        }
    }
    
 // Añade estos métodos a tu CitaService existente

    public List<CitaDTO> obtenerCitasPorUsuario(Long usuarioId) {
        try {
            String url = apiBaseUrl + "/citas/usuario/" + usuarioId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<CitaDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CitaDTO[].class
            );
            
            return Arrays.asList(response.getBody());
            
        } catch (Exception e) {
            System.err.println("Error al obtener citas del usuario: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public CitaDTO obtenerProximaCita(Long usuarioId) {
        try {
            List<CitaDTO> citas = obtenerCitasPorUsuario(usuarioId);
            
            // Filtrar solo citas futuras y con estado "confirmada" o "pendiente"
            LocalDate hoy = LocalDate.now();
            LocalTime ahora = LocalTime.now();
            
            return citas.stream()
                .filter(c -> {
                    LocalDate fechaCita = LocalDate.parse(c.getFecha());
                    return (fechaCita.isAfter(hoy) || 
                           (fechaCita.isEqual(hoy) && LocalTime.parse(c.getHora()).isAfter(ahora)))
                           && (c.getEstado().equalsIgnoreCase("confirmada") || 
                               c.getEstado().equalsIgnoreCase("pendiente"));
                })
                .min((c1, c2) -> {
                    LocalDate fecha1 = LocalDate.parse(c1.getFecha());
                    LocalDate fecha2 = LocalDate.parse(c2.getFecha());
                    int comparacionFecha = fecha1.compareTo(fecha2);
                    if (comparacionFecha != 0) return comparacionFecha;
                    return LocalTime.parse(c1.getHora()).compareTo(LocalTime.parse(c2.getHora()));
                })
                .orElse(null);
                
        } catch (Exception e) {
            System.err.println("Error al obtener próxima cita: " + e.getMessage());
            return null;
        }
    }

    public List<CitaDTO> obtenerHistorialCitas(Long usuarioId, int limite) {
        try {
            List<CitaDTO> todasLasCitas = obtenerCitasPorUsuario(usuarioId);
            
            // Ordenar por fecha descendente y limitar
            return todasLasCitas.stream()
                .sorted((c1, c2) -> {
                    LocalDate fecha1 = LocalDate.parse(c1.getFecha());
                    LocalDate fecha2 = LocalDate.parse(c2.getFecha());
                    return fecha2.compareTo(fecha1); // Descendente
                })
                .limit(limite)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}