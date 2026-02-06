package com.WebEstilo360.WebEstilo360.service;

import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiUsuarioService {

    private final RestTemplate restTemplate;
    //private UsuarioDAO usuarioDAO;
    
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Obtiene todos los usuarios desde la API
     * Requiere token (admin o cliente)
     */
    public List<UsuarioDTO> listarTodosLosUsuarios() {
        try {
            String url = apiBaseUrl + "/usuarios";

            ResponseEntity<List<UsuarioDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UsuarioDTO>>() {}
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            System.err.println("No autorizado para listar usuarios");
            throw new RuntimeException("Sesión expirada. Inicie sesión nuevamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            System.err.println("Sin permisos para listar usuarios");
            throw new RuntimeException("No tiene permisos para ver esta información.");
        } catch (Exception e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene un usuario por su ID
     * Requiere token (admin o cliente)
     */
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        try {
            String url = apiBaseUrl + "/usuarios/" + id;

            ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(
                url,
                UsuarioDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Sesión expirada. Inicie sesión nuevamente.");
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado");
        } catch (Exception e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un nuevo usuario
     * Requiere token de admin
     */
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        try {
            String url = apiBaseUrl + "/usuarios";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioDTO> request = new HttpEntity<>(usuarioDTO, headers);

            ResponseEntity<UsuarioDTO> response = restTemplate.postForEntity(
                url,
                request,
                UsuarioDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Sesión expirada. Inicie sesión nuevamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new RuntimeException("Solo administradores pueden crear usuarios.");
        } catch (HttpClientErrorException.BadRequest e) {
            throw new RuntimeException("Datos inválidos. Verifique los campos.");
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            throw new RuntimeException("Error al crear usuario. Intente nuevamente.");
        }
    }

    /**
     * Actualiza un usuario existente
     * Requiere token de admin
     */
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        try {
            String url = apiBaseUrl + "/usuarios/" + id;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioDTO> request = new HttpEntity<>(usuarioDTO, headers);

            ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                UsuarioDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Sesión expirada. Inicie sesión nuevamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new RuntimeException("Solo administradores pueden actualizar usuarios.");
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado");
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            throw new RuntimeException("Error al actualizar usuario.");
        }
    }

    /**
     * Elimina un usuario
     * Requiere token de admin
     */
    public void eliminarUsuario(Long id) {
        try {
            String url = apiBaseUrl + "/usuarios/" + id;

            restTemplate.delete(url);

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Sesión expirada. Inicie sesión nuevamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new RuntimeException("Solo administradores pueden eliminar usuarios.");
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado");
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            throw new RuntimeException("Error al eliminar usuario.");
        }
    } 
    
}