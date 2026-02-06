package com.estilo360.estilo360.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.estilo360.estilo360.dao.UsuarioDAO;
import com.estilo360.estilo360.dto.LoginRequestDTO;
import com.estilo360.estilo360.dto.LoginResponseDTO;
import com.estilo360.estilo360.entidades.Usuario;
import com.estilo360.estilo360.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio de autenticación de usuarios.
 * Proporciona métodos para realizar login, validar credenciales
 * y generar tokens JWT para usuarios autenticados.
 * 
 * @version 1.0
 */
@Service
public class AuthService {

    /** DAO para acceder a los datos de usuarios */
    private final UsuarioDAO usuarioDAO;

    /** Codificador de contraseñas BCrypt */
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** Utilidad para generación y validación de tokens JWT */
    private JwtUtil jwtUtil;

    /** Logger para registrar eventos de autenticación */
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    /**
     * Constructor que inyecta el DAO de usuarios y la utilidad JWT.
     * 
     * @param usuarioDAO DAO para operaciones sobre usuarios
     * @param jwtUtil Utilidad para generación y validación de tokens JWT
     */
    public AuthService(UsuarioDAO usuarioDAO, JwtUtil jwtUtil) {
        this.usuarioDAO = usuarioDAO;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Realiza el login de un usuario.
     * Valida las credenciales, verifica el estado de verificación de email
     * y genera un token JWT si la autenticación es exitosa.
     * 
     * @param request DTO con el correo y la contraseña del usuario
     * @return LoginResponseDTO que contiene el token JWT generado
     * @throws RuntimeException Si el usuario no existe, la contraseña es incorrecta
     *                          o el email no ha sido verificado
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Intento de login - Usuario: {}", request.getCorreo());

        Usuario usuario = usuarioDAO.findByCorreo(request.getCorreo())
                .orElseThrow(() -> {
                    log.warn("Login fallido - Usuario no encontrado: {}", request.getCorreo());
                    return new RuntimeException("Usuario no encontrado");
                });

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            log.warn("Login fallido - Contraseña incorrecta: {}", request.getCorreo());
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getEmailVerificado()) {
            throw new RuntimeException("Debes verificar tu email antes de iniciar sesión. Revisa tu correo.");
        }

        String token = jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol());

        log.info("Login exitoso - Usuario: {}, Rol: {}", request.getCorreo(), usuario.getRol());
        return new LoginResponseDTO(token);
    }
}