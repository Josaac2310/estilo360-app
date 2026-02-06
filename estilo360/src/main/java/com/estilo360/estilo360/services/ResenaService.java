package com.estilo360.estilo360.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.estilo360.estilo360.dao.ResenaDAO;
import com.estilo360.estilo360.dao.UsuarioDAO;
import com.estilo360.estilo360.dao.EmpleadoDAO;
import com.estilo360.estilo360.dto.ResenaDTO;
import com.estilo360.estilo360.entidades.Resena;
import com.estilo360.estilo360.entidades.Usuario;
import com.estilo360.estilo360.entidades.Empleado;

/**
 * Servicio encargado de la gestión de reseñas.
 * Permite crear, listar y eliminar reseñas, así como obtener reseñas por usuario o empleado.
 * 
 * @version 1.0
 */
@Service
public class ResenaService {

    /** DAO para acceso a datos de reseñas */
    private final ResenaDAO resenaDAO;

    /** DAO para acceso a datos de usuarios */
    private final UsuarioDAO usuarioDAO;

    /** DAO para acceso a datos de empleados */
    private final EmpleadoDAO empleadoDAO;

    /**
     * Constructor que inyecta los DAOs necesarios.
     * 
     * @param resenaDAO DAO de reseñas
     * @param usuarioDAO DAO de usuarios
     * @param empleadoDAO DAO de empleados
     */
    public ResenaService(ResenaDAO resenaDAO, UsuarioDAO usuarioDAO, EmpleadoDAO empleadoDAO) {
        this.resenaDAO = resenaDAO;
        this.usuarioDAO = usuarioDAO;
        this.empleadoDAO = empleadoDAO;
    }

    /**
     * Crea una nueva reseña y la guarda en la base de datos.
     * 
     * @param dto DTO con los datos de la reseña
     * @return DTO de la reseña creada
     * @throws RuntimeException si el usuario o empleado no existen
     */
    public ResenaDTO crearResena(ResenaDTO dto) {
        Usuario usuario = usuarioDAO.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Empleado empleado = empleadoDAO.findById(dto.getEmpleadoId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        Resena resena = new Resena(usuario, empleado, dto.getPuntuacion(), dto.getComentario(), 
                                   dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        resena = resenaDAO.save(resena);

        return new ResenaDTO(
                resena.getId_resena().longValue(),
                usuario.getId_usuario(),
                empleado.getId_empleado(),
                resena.getPuntuacion(),
                resena.getComentario(),
                resena.getFecha()
        );
    }

    /**
     * Obtiene todas las reseñas de la base de datos.
     * 
     * @return Lista de DTOs de todas las reseñas
     */
    public List<ResenaDTO> obtenerTodas() {
        return resenaDAO.findAll().stream()
                .map(r -> new ResenaDTO(
                        r.getId_resena().longValue(),
                        r.getUsuario().getId_usuario(),
                        r.getEmpleado().getId_empleado(),
                        r.getPuntuacion(),
                        r.getComentario(),
                        r.getFecha()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Elimina una reseña por su ID.
     * 
     * @param id ID de la reseña a eliminar
     */
    public void eliminarResena(Long id) {
        resenaDAO.deleteById(id);
    }

    /**
     * Obtiene todas las reseñas de un empleado específico.
     * 
     * @param empleadoId ID del empleado
     * @return Lista de DTOs de reseñas del empleado
     */
    public List<ResenaDTO> obtenerPorEmpleado(Long empleadoId) {
        return resenaDAO.findByEmpleado_IdEmpleado(empleadoId).stream()
                .map(r -> new ResenaDTO(
                        r.getId_resena(),
                        r.getUsuario().getId_usuario(),
                        r.getEmpleado().getId_empleado(),
                        r.getPuntuacion(),
                        r.getComentario(),
                        r.getFecha()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reseñas hechas por un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de DTOs de reseñas realizadas por el usuario
     */
    public List<ResenaDTO> obtenerPorUsuario(Long usuarioId) {
        List<Resena> resenas = resenaDAO.findByUsuarioId(usuarioId);

        return resenas.stream()
                .map(resena -> new ResenaDTO(
                        resena.getId_resena().longValue(),
                        resena.getUsuario().getId_usuario(),
                        resena.getEmpleado().getId_empleado(),
                        resena.getPuntuacion(),
                        resena.getComentario(),
                        resena.getFecha()
                ))
                .collect(Collectors.toList());
    }
}