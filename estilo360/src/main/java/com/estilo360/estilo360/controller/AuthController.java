package com.estilo360.estilo360.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estilo360.estilo360.dao.UsuarioDAO;
import com.estilo360.estilo360.dto.LoginDTO;
import com.estilo360.estilo360.dto.LoginRequestDTO;
import com.estilo360.estilo360.dto.LoginResponseDTO;
import com.estilo360.estilo360.dto.TokenDTO;
import com.estilo360.estilo360.entidades.Usuario;
import com.estilo360.estilo360.security.JwtUtil;
import com.estilo360.estilo360.services.AuthService;
import com.estilo360.estilo360.services.UsuarioService;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST encargado de la autenticación y gestión de seguridad de usuarios.
 * 
 * Expone endpoints para el inicio de sesión, verificación de códigos de correo,
 * solicitud de reseteo de contraseña y actualización de credenciales.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
	
    /** Servicio de negocio para la gestión de usuarios */
    private final UsuarioService usuarioService;
    
    
    /** Servicio de autenticación y generación de tokens */
    private final AuthService authService;

    
    /**
     * Constructor del controlador de autenticación.
     * 
     * @param authService Servicio encargado de la lógica de autenticación
     * @param usuarioService Servicio encargado de la gestión de usuarios
     */
    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para autenticar a un usuario mediante credenciales.
     * 
     * @param loginRequest Objeto con las credenciales de inicio de sesión
     * @return Respuesta con la información de autenticación y token
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        return authService.login(loginRequest);
    }
    
    
    /**
     * Verifica el código de validación enviado al correo electrónico del usuario.
     * 
     * @param request Mapa con el correo y el código de verificación
     * @return Respuesta indicando si el correo fue verificado correctamente
     */
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");
            String codigo = request.get("codigo");

            if (correo == null || codigo == null) {
                return ResponseEntity.badRequest().body("Correo y código son obligatorios");
            }

            boolean verificado = usuarioService.verificarCodigoPorCorreo(correo, codigo);

            if (verificado) {
                return ResponseEntity.ok("Email verificado correctamente");
            } else {
                return ResponseEntity.badRequest().body("Código inválido o expirado");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    
    /**
     * Solicita el envío de un código para el reseteo de la contraseña.
     * 
     * @param request Mapa que contiene el correo del usuario
     * @return Respuesta indicando si el código fue enviado correctamente
     */
    //reestablecer contraseña
    @PostMapping("/solicitar-reset")
    public ResponseEntity<String> solicitarReset(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");

            if (correo == null || correo.isEmpty()) {
                return ResponseEntity.badRequest().body("El correo es obligatorio");
            }

            usuarioService.solicitarResetPassword(correo);
            return ResponseEntity.ok("Código de recuperación enviado");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    
    /**
     * Verifica el código de reseteo de contraseña enviado al correo del usuario.
     * 
     * @param request Mapa con el correo y el código de reseteo
     * @return Respuesta indicando si el código es válido
     */
    @PostMapping("/verificar-codigo-reset")
    public ResponseEntity<String> verificarCodigoReset(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");
            String codigo = request.get("codigo");

            if (correo == null || codigo == null) {
                return ResponseEntity.badRequest().body("Correo y código son obligatorios");
            }

            boolean verificado = usuarioService.verificarCodigoReset(correo, codigo);

            if (verificado) {
                return ResponseEntity.ok("Código válido");
            } else {
                return ResponseEntity.badRequest().body("Código inválido");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    
    /**
     * Permite resetear la contraseña del usuario tras validar el código recibido.
     * 
     * @param request Mapa que contiene el correo, el código y la nueva contraseña
     * @return Respuesta indicando si la contraseña fue actualizada correctamente
     */
    @PostMapping("/resetear-password")
    public ResponseEntity<String> resetearPassword(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");
            String codigo = request.get("codigo");
            String nuevaPassword = request.get("nuevaPassword");

            if (correo == null || codigo == null || nuevaPassword == null) {
                return ResponseEntity.badRequest().body("Todos los campos son obligatorios");
            }

            usuarioService.resetearPassword(correo, codigo, nuevaPassword);
            return ResponseEntity.ok("Contraseña actualizada correctamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}