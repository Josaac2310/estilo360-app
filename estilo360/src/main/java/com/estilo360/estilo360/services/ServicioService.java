package com.estilo360.estilo360.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.estilo360.estilo360.dao.ServicioDAO;
import com.estilo360.estilo360.dto.ServicioDTO;
import com.estilo360.estilo360.entidades.Servicio;

/**
 * Servicio encargado de la gestión de servicios de Estilo360.
 * Permite crear, listar, actualizar, eliminar y obtener servicios.
 * 
 * @version 1.0
 */
@Service
public class ServicioService {

    /** DAO para acceso a datos de servicios */
    private final ServicioDAO servicioDAO;

    /**
     * Constructor que inyecta el DAO de servicios.
     * 
     * @param servicioDAO DAO de servicios
     */
    public ServicioService(ServicioDAO servicioDAO) {
        this.servicioDAO = servicioDAO;
    }

    /**
     * Crea un nuevo servicio y lo guarda en la base de datos.
     * 
     * @param dto DTO con los datos del servicio
     * @return DTO del servicio creado
     */
    public ServicioDTO crearServicio(ServicioDTO dto) {
        Servicio servicio = new Servicio(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getDuracion_minutos()
        );

        servicio = servicioDAO.save(servicio);

        return mapToDTO(servicio);
    }

    /**
     * Obtiene todos los servicios de la base de datos.
     * 
     * @return Lista de DTOs de todos los servicios
     */
    public List<ServicioDTO> listarServicios() {
        return servicioDAO.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un servicio por su ID.
     * 
     * @param id ID del servicio
     * @return DTO del servicio encontrado
     * @throws RuntimeException si el servicio no existe
     */
    public ServicioDTO obtenerPorId(Long id) {
        Servicio servicio = servicioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        return mapToDTO(servicio);
    }

    /**
     * Actualiza un servicio existente.
     * 
     * @param id ID del servicio a actualizar
     * @param dto DTO con los nuevos datos del servicio
     * @return DTO del servicio actualizado
     * @throws RuntimeException si el servicio no existe
     */
    public ServicioDTO actualizarServicio(Long id, ServicioDTO dto) {
        Servicio servicio = servicioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracion_minutos(dto.getDuracion_minutos());

        servicio = servicioDAO.save(servicio);
        return mapToDTO(servicio);
    }

    /**
     * Elimina un servicio por su ID.
     * 
     * @param id ID del servicio a eliminar
     * @throws RuntimeException si el servicio no existe
     */
    public void eliminarServicio(Long id) {
        if (!servicioDAO.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado");
        }
        servicioDAO.deleteById(id);
    }

    /**
     * Mapea una entidad Servicio a su DTO correspondiente.
     * 
     * @param servicio Entidad Servicio
     * @return DTO de Servicio
     */
    private ServicioDTO mapToDTO(Servicio servicio) {
        return new ServicioDTO(
                servicio.getId_servicio(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getDuracion_minutos()
        );
    }
}