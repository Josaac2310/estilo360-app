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

/**
 * Servicio para interactuar con la API de usuarios.
 * Contiene métodos para listar, obtener, crear, actualizar y eliminar usuarios,
 * así como para obtener el perfil del usuario actual.
 * Este servicio maneja la comunicación HTTP y la lógica de manejo de errores
 * relacionados con la API externa.
 * 
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApiUsuarioService {

    /** Cliente HTTP para realizar solicitudes REST */
    private final RestTemplate restTemplate;

    /** URL base de la API, inyectada desde configuración */
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Obtiene todos los usuarios desde la API.
     * Requiere token válido de admin o cliente.
     * 
     * @return Lista de usuarios obtenida de la API. Puede ser vacía si ocurre un error.
     * @throws RuntimeException Si la sesión ha expirado o el usuario no tiene permisos
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
     * Obtiene un usuario por su ID desde la API.
     * Requiere token válido de admin o cliente.
     * 
     * @param id Identificador único del usuario a obtener
     * @return UsuarioDTO correspondiente al ID, o null si ocurre un error
     * @throws RuntimeException Si la sesión ha expirado o el usuario no existe
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
     * Crea un nuevo usuario en la API.
     * Requiere token de administrador.
     * 
     * @param usuarioDTO Datos del usuario a crear
     * @return UsuarioDTO creado con los datos devueltos por la API
     * @throws RuntimeException Si la sesión ha expirado, el usuario no tiene permisos
     *                          o los datos enviados son inválidos
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
     * Actualiza un usuario existente en la API.
     * Requiere token de administrador.
     * 
     * @param id Identificador del usuario a actualizar
     * @param usuarioDTO Datos del usuario para actualizar
     * @return UsuarioDTO actualizado devuelto por la API
     * @throws RuntimeException Si la sesión ha expirado, el usuario no tiene permisos
     *                          o el usuario no existe
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
     * Elimina un usuario en la API.
     * Requiere token de administrador.
     * 
     * @param id Identificador del usuario a eliminar
     * @throws RuntimeException Si la sesión ha expirado, el usuario no tiene permisos
     *                          o el usuario no existe
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
    
    /**
     * Obtiene el perfil del usuario actual usando un token JWT.
     * 
     * @param token Token JWT del usuario
     * @return UsuarioDTO con los datos del perfil, o null si ocurre un error
     * @throws RuntimeException Si la sesión ha expirado
     */
    public UsuarioDTO obtenerPerfil(String token) {
        try {
            String url = apiBaseUrl + "/usuarios/perfil";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                UsuarioDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Sesión expirada. Inicie sesión nuevamente.");
        } catch (Exception e) {
            System.err.println("Error al obtener perfil: " + e.getMessage());
            return null;
        }
    }
}