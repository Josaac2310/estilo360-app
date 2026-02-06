package com.estilo360.estilo360.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estilo360.estilo360.dao.UsuarioDAO;
import com.estilo360.estilo360.dto.UsuarioDTO;
import com.estilo360.estilo360.entidades.Usuario;
import com.estilo360.estilo360.services.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;



/**
 * Controlador REST para la gestión de usuarios.
 * 
 * Expone endpoints para realizar operaciones CRUD sobre usuarios,
 * así como para la búsqueda por correo electrónico y la obtención
 * del perfil del usuario autenticado.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	
    /** Servicio de negocio encargado de la gestión de usuarios */
    private final UsuarioService usuarioService;
    
    /** DAO para el acceso directo a datos de usuarios */
    private UsuarioDAO usuarioDAO;
    
    /**
     * Constructor del controlador de usuarios.
     * 
     * @param usuarioService Servicio que contiene la lógica de negocio de usuarios
     * @param usuarioDAO DAO encargado del acceso a datos de usuarios
     */
    public UsuarioController(UsuarioService usuarioService, UsuarioDAO usuarioDAO) {
        this.usuarioService = usuarioService;
        this.usuarioDAO = usuarioDAO;
    }
   

    /**
     * Lista todos los usuarios registrados.
     * 
     * @return Lista de usuarios
     */
    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }
    
    /**
     * Obtiene un usuario por su identificador.
     * 
     * @param id Identificador único del usuario
     * @return Usuario correspondiente al identificador
     */
    @GetMapping("/{id}")
    public UsuarioDTO obtenerUsuario(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }
    
    /**
     * Crea un nuevo usuario.
     * 
     * @param usuarioDTO Objeto con la información del usuario a crear
     * @return Usuario creado
     */
    @PostMapping
    public UsuarioDTO crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.crearUsuario(usuarioDTO);
    }

    
 // Actualizar usuario
    /**
     * Actualiza la información de un usuario existente.
     * 
     * @param id Identificador único del usuario
     * @param usuarioDTO Objeto con los nuevos datos del usuario
     * @return Usuario actualizado
     */
    @PutMapping("/{id}")
    public UsuarioDTO actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizarUsuario(id, usuarioDTO);
    }

    // Eliminar usuario
    // Eliminar usuario
    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id Identificador único del usuario a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }
    
    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param correo Correo electrónico del usuario
     * @return Usuario correspondiente al correo indicado
     * @throws RuntimeException Si el usuario no es encontrado
     */
    @GetMapping("/buscar-por-correo")
    public UsuarioDTO buscarPorCorreo(@RequestParam String correo) {
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
    
    /**
     * Obtiene el perfil del usuario autenticado a partir del token JWT.
     * 
     * @param request Petición HTTP que contiene el token de autenticación
     * @return Perfil del usuario autenticado
     */
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> obtenerPerfil(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(usuarioService.obtenerPerfilPorToken(token));
    }
}
