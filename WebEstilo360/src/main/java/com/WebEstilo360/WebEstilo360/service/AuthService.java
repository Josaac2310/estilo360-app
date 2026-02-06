package com.WebEstilo360.WebEstilo360.service;

import com.WebEstilo360.WebEstilo360.dto.LoginRequestDTO;
import com.WebEstilo360.WebEstilo360.dto.LoginResponseDTO;
import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio de autenticación y gestión de usuarios.
 * Proporciona métodos para login, registro, verificación de códigos y
 * recuperación de contraseña mediante comunicación con la API externa.
 * Maneja los errores comunes relacionados con autenticación y autorización.
 * 
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /** Cliente HTTP para realizar solicitudes REST */
    private final RestTemplate restTemplate;

    /** URL base de la API, inyectada desde configuración */
    @Value("${api.base.url}")
    private String apiBaseUrl;

    /**
     * Realiza el login del usuario contra la API y devuelve el token.
     * 
     * @param loginRequest Datos de login (correo y contraseña)
     * @return LoginResponseDTO que contiene el token y otros datos
     * @throws RuntimeException Si las credenciales son incorrectas, los datos son inválidos
     *                          o hay un error de conexión
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            String url = apiBaseUrl + "/auth/login";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LoginRequestDTO> request = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
                url,
                request,
                LoginResponseDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            // Credenciales incorrectas
            throw new RuntimeException("Correo o contraseña incorrectos");
        } catch (HttpClientErrorException.BadRequest e) {
            // Error de validación
            throw new RuntimeException("Datos de login inválidos");
        } catch (Exception e) {
            // Error de conexión u otro
            System.err.println("Error al conectar con la API: " + e.getMessage());
            throw new RuntimeException("Error al conectar con el servidor. Intente más tarde.");
        }
    }
    
    /**
     * Verifica un código enviado al correo del usuario.
     * 
     * @param correo Correo del usuario
     * @param codigo Código a verificar
     * @return true si el código es válido, false en caso contrario
     */
    public boolean verificarCodigo(String correo, String codigo) {
        try {
            String url = apiBaseUrl + "/auth/verificar-codigo";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Crear el body con correo y código
            Map<String, String> body = new HashMap<>();
            body.put("correo", correo);
            body.put("codigo", codigo);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
            
        } catch (Exception e) {
            System.err.println("Error al verificar código: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Registra un nuevo usuario en la API.
     * 
     * @param usuarioDTO Datos del usuario a registrar
     * @throws RuntimeException Si los datos son inválidos o el correo ya está registrado
     */
    public void register(UsuarioDTO usuarioDTO) {
        try {
            String url = apiBaseUrl + "/usuarios";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioDTO> request = new HttpEntity<>(usuarioDTO, headers);

            restTemplate.postForEntity(url, request, Void.class);

        } catch (HttpClientErrorException.BadRequest e) {
            throw new RuntimeException("Datos inválidos o correo ya registrado");
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            throw new RuntimeException("Error al crear la cuenta");
        }
    }
    
    /**
     * Solicita el envío de un código para resetear la contraseña.
     * 
     * @param correo Correo del usuario que solicita el reset
     * @throws RuntimeException Si ocurre un error al procesar la solicitud
     */
    public void solicitarResetPassword(String correo) {
        try {
            String url = apiBaseUrl + "/auth/solicitar-reset";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, String> body = new HashMap<>();
            body.put("correo", correo);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            
            restTemplate.postForEntity(url, request, String.class);
            
        } catch (Exception e) {
            System.err.println("Error al solicitar reset: " + e.getMessage());
            throw new RuntimeException("Error al procesar la solicitud");
        }
    }

    /**
     * Verifica el código recibido para resetear la contraseña.
     * 
     * @param correo Correo del usuario
     * @param codigo Código recibido para verificación
     * @return true si el código es válido, false en caso contrario
     */
    public boolean verificarCodigoReset(String correo, String codigo) {
        try {
            String url = apiBaseUrl + "/auth/verificar-codigo-reset";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, String> body = new HashMap<>();
            body.put("correo", correo);
            body.put("codigo", codigo);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
            
        } catch (Exception e) {
            System.err.println("Error al verificar código reset: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambia la contraseña del usuario usando un código de verificación.
     * 
     * @param correo Correo del usuario
     * @param codigo Código de verificación
     * @param nuevaPassword Nueva contraseña a establecer
     * @throws RuntimeException Si ocurre un error al cambiar la contraseña
     */
    public void resetearPassword(String correo, String codigo, String nuevaPassword) {
        try {
            String url = apiBaseUrl + "/auth/resetear-password";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, String> body = new HashMap<>();
            body.put("correo", correo);
            body.put("codigo", codigo);
            body.put("nuevaPassword", nuevaPassword);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            
            restTemplate.postForEntity(url, request, String.class);
            
        } catch (Exception e) {
            System.err.println("Error al resetear password: " + e.getMessage());
            throw new RuntimeException("Error al cambiar la contraseña");
        }
    }
    
    /**
     * Obtiene los datos de un usuario por su correo.
     * 
     * @param correo Correo del usuario
     * @return UsuarioDTO correspondiente al correo, o null si ocurre un error
     */
    public UsuarioDTO obtenerUsuarioPorCorreo(String correo) {
        try {
            String url = apiBaseUrl + "/usuarios/buscar-por-correo?correo=" + correo;
            
            ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(url, UsuarioDTO.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            System.err.println("Error al obtener usuario por correo: " + e.getMessage());
            return null;
        }
    }
}