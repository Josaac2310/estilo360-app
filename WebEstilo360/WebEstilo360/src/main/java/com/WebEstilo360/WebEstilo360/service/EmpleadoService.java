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

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

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