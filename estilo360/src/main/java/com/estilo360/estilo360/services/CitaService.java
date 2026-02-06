package com.estilo360.estilo360.services;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.estilo360.estilo360.dao.*;
import com.estilo360.estilo360.dto.CitaDTO;
import com.estilo360.estilo360.entidades.*;
import com.estilo360.estilo360.security.JwtUtil;

import java.util.ArrayList;
import java.util.Comparator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Servicio de negocio para la gestión de citas.
 * Proporciona métodos para crear, actualizar, listar y eliminar citas,
 * así como para obtener historial, próximas citas y horarios disponibles.
 * 
 * @version 1.0
 */
@Service
public class CitaService {

    /** DAO para acceso a los datos de citas */
    private final CitaDAO citaDAO;

    /** DAO para acceso a los datos de usuarios */
    private final UsuarioDAO usuarioDAO;

    /** DAO para acceso a los datos de empleados */
    private final EmpleadoDAO empleadoDAO;

    /** DAO para acceso a los datos de servicios */
    private final ServicioDAO servicioDAO;

    /** Utilidad para operaciones con JWT */
    private final JwtUtil jwtUtil;

    /** Formateador de fechas (dd/MM/yyyy) */
    private final DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formateador de horas (HH:mm) */
    private final DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /** Logger para registrar eventos */
    private static final Logger log = LoggerFactory.getLogger(CitaService.class);

    /**
     * Constructor que inyecta los DAOs y la utilidad JWT.
     * 
     * @param citaDAO DAO para operaciones sobre citas
     * @param usuarioDAO DAO para operaciones sobre usuarios
     * @param empleadoDAO DAO para operaciones sobre empleados
     * @param servicioDAO DAO para operaciones sobre servicios
     * @param jwtUtil Utilidad para generar y validar tokens JWT
     */
    public CitaService(
            CitaDAO citaDAO,
            UsuarioDAO usuarioDAO,
            EmpleadoDAO empleadoDAO,
            ServicioDAO servicioDAO,
            JwtUtil jwtUtil) {
        this.citaDAO = citaDAO;
        this.usuarioDAO = usuarioDAO;
        this.empleadoDAO = empleadoDAO;
        this.servicioDAO = servicioDAO;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Crea una nueva cita a partir de un DTO.
     * Valida la existencia de usuario, empleado y servicio, y persiste la cita.
     * 
     * @param dto Datos de la cita a crear
     * @return CitaDTO con la información de la cita creada
     * @throws RuntimeException Si el usuario, empleado o servicio no existen
     */
    public CitaDTO crearCita(CitaDTO dto) {
        log.info("Creando cita - Usuario ID: {}, Servicio ID: {}, Fecha: {}", 
                dto.getUsuario_id(), dto.getServicio_id(), dto.getFecha());

        Usuario usuario = usuarioDAO.findById(dto.getUsuario_id())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        Empleado empleado = empleadoDAO.findById(dto.getEmpleado_id())
                .orElseThrow(() -> new RuntimeException("Empleado no existe"));

        Servicio servicio = servicioDAO.findById(dto.getServicio_id())
                .orElseThrow(() -> new RuntimeException("Servicio no existe"));

        LocalDate fecha = LocalDate.parse(dto.getFecha(), fechaFormatter);
        LocalTime hora = LocalTime.parse(dto.getHora(), horaFormatter);

        Cita cita = new Cita(
                usuario,
                empleado,
                servicio,
                fecha,
                hora,
                "pendiente",
                dto.getObservaciones()
        );

        cita = citaDAO.save(cita);
        log.info("Cita creada exitosamente - ID: {}", cita.getId_cita());

        return mapToDTO(cita);
    }

    /**
     * Lista todas las citas ordenadas por fecha y hora descendente.
     * 
     * @return Lista de CitaDTO
     */
    public List<CitaDTO> listarCitas() {
        return citaDAO.findAllByOrderByFechaDescHoraDesc()  
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una cita por su ID.
     * 
     * @param id Identificador de la cita
     * @return CitaDTO con la información de la cita
     * @throws RuntimeException Si la cita no existe
     */
    public CitaDTO obtenerPorId(Long id) {
        Cita cita = citaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return mapToDTO(cita);
    }

    /**
     * Elimina una cita por su ID.
     * 
     * @param id Identificador de la cita a eliminar
     */
    public void eliminarCita(Long id) {
        citaDAO.deleteById(id);
        log.info("Eliminando cita - ID: {}", id);
    }

    /**
     * Convierte una entidad Cita a su correspondiente DTO.
     * Incluye nombres del empleado y servicio.
     * 
     * @param cita Entidad Cita
     * @return CitaDTO mapeado
     */
    private CitaDTO mapToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO(
                cita.getId_cita(),
                cita.getUsuario().getId_usuario(),
                cita.getEmpleado().getId_empleado(),
                cita.getServicio().getId_servicio(),
                cita.getFecha().format(fechaFormatter),
                cita.getHora().format(horaFormatter),
                cita.getEstado(),
                cita.getObservaciones()
        );

        dto.setNombreEmpleado(cita.getEmpleado().getNombre_completo());
        dto.setNombreServicio(cita.getServicio().getNombre());

        return dto;
    }

    /**
     * Actualiza una cita existente.
     * 
     * @param id ID de la cita a actualizar
     * @param dto Datos actualizados de la cita
     * @return CitaDTO actualizado
     * @throws RuntimeException Si la cita, usuario, empleado o servicio no existen
     */
    public CitaDTO actualizarCita(Long id, CitaDTO dto) {
        Cita cita = citaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Usuario usuario = usuarioDAO.findById(dto.getUsuario_id())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        Empleado empleado = empleadoDAO.findById(dto.getEmpleado_id())
                .orElseThrow(() -> new RuntimeException("Empleado no existe"));

        Servicio servicio = servicioDAO.findById(dto.getServicio_id())
                .orElseThrow(() -> new RuntimeException("Servicio no existe"));

        LocalDate fecha = LocalDate.parse(dto.getFecha(), fechaFormatter);
        LocalTime hora = LocalTime.parse(dto.getHora(), horaFormatter);

        cita.setUsuario(usuario);
        cita.setEmpleado(empleado);
        cita.setServicio(servicio);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEstado(dto.getEstado());
        cita.setObservaciones(dto.getObservaciones());

        cita = citaDAO.save(cita);

        return mapToDTO(cita);
    }

    /**
     * Obtiene el historial de citas pasadas de un usuario limitado a N resultados.
     * 
     * @param token Token JWT del usuario
     * @param limite Cantidad máxima de citas a retornar
     * @return Lista de CitaDTO
     * @throws RuntimeException Si el usuario no existe
     */
    public List<CitaDTO> obtenerHistorialUsuario(String token, int limite) {
        String correo = jwtUtil.obtenerCorreo(token);
        Usuario usuario = usuarioDAO.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return citaDAO.findAll().stream()
                .filter(c -> c.getUsuario().getId_usuario().equals(usuario.getId_usuario()))
                .filter(c -> c.getFecha().isBefore(hoy) || 
                            (c.getFecha().isEqual(hoy) && c.getHora().isBefore(ahora)))
                .sorted(Comparator.comparing(Cita::getFecha)
                                 .thenComparing(Cita::getHora)
                                 .reversed())
                .limit(limite)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las citas de un usuario.
     * 
     * @param token Token JWT del usuario
     * @return Lista de CitaDTO
     * @throws RuntimeException Si el usuario no existe
     */
    public List<CitaDTO> obtenerCitasUsuario(String token) {
        String correo = jwtUtil.obtenerCorreo(token);
        Usuario usuario = usuarioDAO.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return citaDAO.findAll().stream()
                .filter(c -> c.getUsuario().getId_usuario().equals(usuario.getId_usuario()))
                .sorted(Comparator.comparing(Cita::getFecha)
                                 .thenComparing(Cita::getHora)
                                 .reversed())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la próxima cita futura de un usuario que no esté cancelada.
     * 
     * @param token Token JWT del usuario
     * @return CitaDTO de la próxima cita o null si no existe
     * @throws RuntimeException Si el usuario no existe
     */
    public CitaDTO obtenerProximaCita(String token) {
        String correo = jwtUtil.obtenerCorreo(token);
        Usuario usuario = usuarioDAO.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return citaDAO.findAll().stream()
                .filter(c -> c.getUsuario().getId_usuario().equals(usuario.getId_usuario()))
                .filter(c -> !c.getEstado().equalsIgnoreCase("cancelada"))
                .filter(c -> c.getFecha().isAfter(hoy) || 
                            (c.getFecha().isEqual(hoy) && c.getHora().isAfter(ahora)))
                .min(Comparator.comparing(Cita::getFecha)
                              .thenComparing(Cita::getHora))
                .map(this::mapToDTO)
                .orElse(null);
    }

    /**
     * Obtiene los horarios disponibles de un empleado en una fecha específica.
     * 
     * @param empleadoId ID del empleado
     * @param fecha Fecha en formato "dd/MM/yyyy"
     * @return Lista de horarios disponibles en formato "HH:mm"
     */
    public List<String> obtenerHorariosDisponibles(Long empleadoId, String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaBuscada = LocalDate.parse(fecha, formatter);

        List<Cita> citasDelDia = citaDAO.findByEmpleado_IdEmpleadoAndFecha(empleadoId, fechaBuscada);

        List<String> todosLosHorarios = generarHorarios();

        Set<String> horariosOcupados = citasDelDia.stream()
                .map(cita -> cita.getHora().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toSet());

        return todosLosHorarios.stream()
                .filter(horario -> !horariosOcupados.contains(horario))
                .collect(Collectors.toList());
    }

    /**
     * Genera la lista de horarios posibles para citas (9:00 a 20:00 cada 30 minutos).
     * 
     * @return Lista de horarios en formato "HH:mm"
     */
    private List<String> generarHorarios() {
        List<String> horarios = new ArrayList<>();
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(20, 0);

        while (!inicio.isAfter(fin)) {
            horarios.add(inicio.format(DateTimeFormatter.ofPattern("HH:mm")));
            inicio = inicio.plusMinutes(30);
        }

        return horarios;
    }
}
