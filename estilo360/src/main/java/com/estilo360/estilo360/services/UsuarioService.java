package com.estilo360.estilo360.services;


import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.estilo360.estilo360.dao.UsuarioDAO;
import com.estilo360.estilo360.dto.UsuarioDTO;
import com.estilo360.estilo360.entidades.Usuario;
import com.estilo360.estilo360.security.JwtUtil;


/**
 * Servicio encargado de la gestión de usuarios de Estilo360.
 * Permite crear, actualizar, eliminar, verificar email, 
 * restablecer contraseña y obtener perfiles de usuario.
 * 
 * @version 1.0
 */
@Service
public class UsuarioService {

    /** DAO para acceso a datos de usuarios */
    private final UsuarioDAO usuarioDAO;
    
    /** Encriptador de contraseñas */
    private final BCryptPasswordEncoder passwordEncoder;
    
    /** Servicio de correo para enviar emails de verificación y reset */
    private final EmailService emailService; // ← AGREGAR

    /** Utilidad JWT para obtener información del token */
    private final JwtUtil jwtUtil;
    
    
    /**
     * Constructor que inyecta DAO de usuarios, servicio de correo y utilidades JWT.
     * 
     * @param usuarioDAO DAO de usuarios
     * @param emailService Servicio de email
     * @param jwtUtil Utilidad JWT
     */
    public UsuarioService(UsuarioDAO usuarioDAO, EmailService emailService, JwtUtil jwtUtil) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;  
    }
    
    
    
    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return DTO del usuario
     * @throws RuntimeException si el usuario no existe
     */
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UsuarioDTO(
                usuario.getId_usuario(),
                usuario.getNombre_completo(),
                usuario.getCorreo(),
                usuario.getMovil(),
                usuario.getRol(),
                null
        );
    }



    /**
     * Crea un nuevo usuario, encripta su contraseña, genera código de verificación
     * y envía un email de confirmación.
     * 
     * @param usuarioDTO DTO con los datos del usuario
     * @return DTO del usuario creado
     */
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Encriptar contraseña
        String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getContrasena());

        // Generar código de 6 dígitos aleatorio ← CAMBIAR ESTO
        String codigoVerificacion = String.format("%06d", new Random().nextInt(999999));

        // Crear entidad Usuario
        Usuario usuario = new Usuario(
                usuarioDTO.getNombre_completo(),
                usuarioDTO.getCorreo(),
                usuarioDTO.getMovil(),
                usuarioDTO.getRol(),
                passwordEncriptada
        );

        // Configurar verificación
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacion(codigoVerificacion); // ← Ahora guarda código de 6 dígitos

        // Guardar en DB
        usuario = usuarioDAO.save(usuario);

        // Enviar email de verificación con código
        try {
            emailService.enviarEmailVerificacion(usuario.getCorreo(), codigoVerificacion);
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }

        // Crear DTO con el ID generado
        return new UsuarioDTO(
                usuario.getId_usuario(),
                usuario.getNombre_completo(),
                usuario.getCorreo(),
                usuario.getMovil(),
                usuario.getRol(),
                null
        );
    }
    


    /**
     * Verifica el código enviado al correo del usuario y activa su cuenta.
     * 
     * @param correo Correo del usuario
     * @param codigo Código de verificación
     * @return true si la verificación fue exitosa, false en caso contrario
     */
    public boolean verificarCodigoPorCorreo(String correo, String codigo) {
        try {
            // Buscar usuario por correo
            Usuario usuario = usuarioDAO.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que el código coincida
            if (usuario.getTokenVerificacion() == null || 
                !usuario.getTokenVerificacion().equals(codigo)) {
                throw new RuntimeException("Código inválido");
            }

            // Verificar email
            usuario.setEmailVerificado(true);
            usuario.setTokenVerificacion(null); // Limpiar el código
            usuarioDAO.save(usuario);

            return true;

        } catch (Exception e) {
            System.err.println("Error al verificar código: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Valida que el rol sea "admin" o "cliente".
     * 
     * @param rol Rol a validar
     * @throws RuntimeException si el rol no es válido
     */
    private void validarRol(String rol) {
        if (!rol.equalsIgnoreCase("admin") && !rol.equalsIgnoreCase("cliente")) {
            throw new RuntimeException("Rol no válido. Solo admin o cliente");
        }
    }


    /**
     * Obtiene todos los usuarios registrados.
     * 
     * @return Lista de DTOs de usuarios
     */
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioDAO.findAll()
                .stream()
                .map(u -> new UsuarioDTO(
                		u.getId_usuario(),
                        u.getNombre_completo(),
                        u.getCorreo(),
                        u.getMovil(),
                        u.getRol(),
                        null
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza los datos de un usuario existente.
     * 
     * @param id ID del usuario a actualizar
     * @param usuarioDTO DTO con los nuevos datos
     * @return DTO del usuario actualizado
     */
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre_completo(usuarioDTO.getNombre_completo());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setMovil(usuarioDTO.getMovil());
        usuario.setRol(usuarioDTO.getRol());

        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().isEmpty()) {
            String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getContrasena());
            usuario.setContrasena(passwordEncriptada);
        }

        usuario = usuarioDAO.save(usuario);

        return new UsuarioDTO(
                usuario.getId_usuario(),
                usuario.getNombre_completo(),
                usuario.getCorreo(),
                usuario.getMovil(),
                usuario.getRol(),
                null
        );
    }
    // Eliminar usuario
    /**
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     * @throws RuntimeException si el usuario no existe
     */
    public void eliminarUsuario(Long id) {
        if (!usuarioDAO.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioDAO.deleteById(id);
    }

    // Helper para mapear Usuario → UsuarioDTO
    /**
     * Mapea un objeto Usuario a su DTO correspondiente.
     * 
     * @param usuario Entidad Usuario
     * @return DTO de Usuario
     */
    private UsuarioDTO mapToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId_usuario(),         // <-- ID agregado
                usuario.getNombre_completo(),
                usuario.getCorreo(),
                usuario.getMovil(),
                usuario.getRol(),
                null // nunca devolvemos la contraseña
        );
    }
    
    //reestablecer contraseña 
 // Solicitar reset de contraseña - genera código y envía email
    /**
     * Solicita un reseteo de contraseña generando un código y enviándolo al correo.
     * 
     * @param correo Correo del usuario
     */
    public void solicitarResetPassword(String correo) {
        try {
            // Buscar usuario por correo
            Usuario usuario = usuarioDAO.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

            // Generar código de 6 dígitos
            String codigoReset = String.format("%06d", new Random().nextInt(999999));

            // Guardar el código en token_verificacion
            usuario.setTokenVerificacion(codigoReset);
            usuarioDAO.save(usuario);

            // Enviar email con código
            emailService.enviarEmailResetPassword(correo, codigoReset);

        } catch (Exception e) {
            System.err.println("Error al solicitar reset: " + e.getMessage());
            throw new RuntimeException("Error al procesar la solicitud");
        }
    }

    // Verificar código de reset
    /**
     * Verifica el código de reseteo enviado al correo del usuario.
     * 
     * @param correo Correo del usuario
     * @param codigo Código de reseteo
     * @return true si el código es correcto, false si es incorrecto o inexistente
     */
    public boolean verificarCodigoReset(String correo, String codigo) {
        try {
            // Buscar usuario por correo
            Usuario usuario = usuarioDAO.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que el código coincida
            if (usuario.getTokenVerificacion() == null || 
                !usuario.getTokenVerificacion().equals(codigo)) {
                return false;
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error al verificar código reset: " + e.getMessage());
            return false;
        }
    }

    // Resetear contraseña
    /**
     * Restablece la contraseña del usuario verificando el código enviado.
     * 
     * @param correo Correo del usuario
     * @param codigo Código de verificación
     * @param nuevaPassword Nueva contraseña a establecer
     */
    public void resetearPassword(String correo, String codigo, String nuevaPassword) {
        try {
            // Buscar usuario por correo
            Usuario usuario = usuarioDAO.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar código
            if (usuario.getTokenVerificacion() == null || 
                !usuario.getTokenVerificacion().equals(codigo)) {
                throw new RuntimeException("Código inválido o expirado");
            }

            // Encriptar nueva contraseña
            String passwordEncriptada = passwordEncoder.encode(nuevaPassword);

            // Actualizar contraseña y limpiar token
            usuario.setContrasena(passwordEncriptada);
            usuario.setTokenVerificacion(null);
            usuarioDAO.save(usuario);

        } catch (Exception e) {
            System.err.println("Error al resetear password: " + e.getMessage());
            throw new RuntimeException("Error al cambiar la contraseña");
        }
    }
    
    //añadir
    /**
     * Obtiene el perfil del usuario a partir del token JWT.
     * 
     * @param token Token JWT del usuario
     * @return DTO del perfil del usuario
     */
    public UsuarioDTO obtenerPerfilPorToken(String token) {
        String correo = jwtUtil.obtenerCorreo(token);
        Usuario usuario = usuarioDAO.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UsuarioDTO(
                usuario.getId_usuario(),
                usuario.getNombre_completo(),
                usuario.getCorreo(),
                usuario.getMovil(),
                usuario.getRol(),
                null
        );
    }
    
}