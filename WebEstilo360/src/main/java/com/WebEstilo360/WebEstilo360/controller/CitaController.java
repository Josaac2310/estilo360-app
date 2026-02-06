package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.CitaDTO;
import com.WebEstilo360.WebEstilo360.dto.EmpleadoDTO;
import com.WebEstilo360.WebEstilo360.dto.ServicioDTO;
import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import com.WebEstilo360.WebEstilo360.service.ApiUsuarioService;
import com.WebEstilo360.WebEstilo360.service.CitaService;
import com.WebEstilo360.WebEstilo360.service.EmpleadoService;
import com.WebEstilo360.WebEstilo360.service.ServicioService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controlador MVC para la gestión de citas.
 * 
 * Gestiona endpoints para listar, crear, editar y eliminar citas.
 * Interactúa con CitaService, ApiUsuarioService, EmpleadoService y ServicioService.
 * @version 1.0
 */
@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    /** Servicio para operaciones de citas */
    private final CitaService citaService;

    /** Servicio para obtener información de usuarios */
    private final ApiUsuarioService usuarioService;

    /** Servicio para obtener información de empleados */
    private final EmpleadoService empleadoService;

    /** Servicio para obtener información de servicios */
    private final ServicioService servicioService;

    /** Logger para seguimiento de eventos en el controlador */
    Logger logger = LoggerFactory.getLogger(DashboardController.class); // Ajusta al controlador real

    /**
     * Lista todas las citas junto con información de usuarios, empleados y servicios.
     * 
     * @param session Sesión HTTP del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "citas" o redirección a login si no está autenticado
     */
    @GetMapping
    public String listarCitas(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            List<CitaDTO> citas = citaService.listarCitas();
            List<UsuarioDTO> usuarios = usuarioService.listarTodosLosUsuarios();
            List<EmpleadoDTO> empleados = empleadoService.listarEmpleados();
            List<ServicioDTO> servicios = servicioService.listarServicios();

            Map<Long, String> usuariosMap = new HashMap<>();
            Map<Long, String> empleadosMap = new HashMap<>();
            Map<Long, String> serviciosMap = new HashMap<>();

            usuarios.forEach(u -> usuariosMap.put(u.getId_usuario(), u.getNombre_completo()));
            empleados.forEach(e -> empleadosMap.put(e.getId_empleado(), e.getNombre_completo()));
            servicios.forEach(s -> serviciosMap.put(s.getId_servicio(), s.getNombre()));

            citas.forEach(cita -> {
                cita.setNombreUsuario(usuariosMap.get(cita.getUsuario_id()));
                cita.setNombreEmpleado(empleadosMap.get(cita.getEmpleado_id()));
                cita.setNombreServicio(serviciosMap.get(cita.getServicio_id()));
            });

            model.addAttribute("citas", citas);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));

            return "citas";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las citas: " + e.getMessage());
            return "citas";
        }
    }

    /**
     * Muestra el formulario para editar una cita existente.
     * 
     * @param id Identificador de la cita a editar
     * @param session Sesión HTTP del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "editar-cita" o redirección a login/citas
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          HttpSession session,
                                          Model model) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            CitaDTO cita = citaService.obtenerCitaPorId(id);
            
            if (cita == null) {
                return "redirect:/citas";
            }

            model.addAttribute("cita", cita);
            model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios());
            model.addAttribute("empleados", empleadoService.listarEmpleados());
            model.addAttribute("servicios", servicioService.listarServicios());
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));

            return "editar-cita";

        } catch (Exception e) {
            return "redirect:/citas";
        }
    }

    /**
     * Procesa la edición de una cita existente.
     * 
     * @param id Identificador de la cita
     * @param usuario_id ID del usuario asociado
     * @param empleado_id ID del empleado asociado
     * @param servicio_id ID del servicio asociado
     * @param fecha Fecha de la cita en formato DD/MM/YYYY
     * @param hora Hora de la cita
     * @param session Sesión HTTP del usuario
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a la lista de citas
     */
    @PostMapping("/editar/{id}")
    public String editarCita(@PathVariable Long id,
                            @RequestParam Long usuario_id,
                            @RequestParam Long empleado_id,
                            @RequestParam Long servicio_id,
                            @RequestParam String fecha,
                            @RequestParam String hora,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            CitaDTO citaDTO = new CitaDTO();
            citaDTO.setId_cita(id);
            citaDTO.setUsuario_id(usuario_id);
            citaDTO.setEmpleado_id(empleado_id);
            citaDTO.setServicio_id(servicio_id);
            citaDTO.setFecha(fecha);
            citaDTO.setHora(hora);

            CitaDTO citaActual = citaService.obtenerCitaPorId(id);
            citaDTO.setEstado(citaActual.getEstado());
            citaDTO.setObservaciones(citaActual.getObservaciones());

            citaService.actualizarCita(id, citaDTO);
            redirectAttributes.addFlashAttribute("success", "Cita actualizada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la cita: " + e.getMessage());
        }

        return "redirect:/citas";
    }

    /**
     * Elimina una cita por su ID.
     * 
     * @param id Identificador de la cita
     * @param session Sesión HTTP del usuario
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a la lista de citas
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            citaService.eliminarCita(id);
            redirectAttributes.addFlashAttribute("success", "Cita eliminada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cita");
        }

        return "redirect:/citas";
    }

    /**
     * Muestra el formulario para crear una nueva cita.
     * 
     * @param session Sesión HTTP del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "cliente-nuevacita"
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCita(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            String token = SessionUtil.obtenerToken(session);
            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            model.addAttribute("usuario", usuario);

            List<ServicioDTO> servicios = servicioService.listarServicios();
            List<EmpleadoDTO> empleados = empleadoService.listarEmpleados();

            model.addAttribute("servicios", servicios);
            model.addAttribute("empleados", empleados);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));

            return "cliente-nuevacita";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    /**
     * Obtiene los horarios disponibles para un empleado en una fecha específica.
     * 
     * @param empleadoId ID del empleado
     * @param fecha Fecha en formato DD/MM/YYYY
     * @param session Sesión HTTP del usuario
     * @return Lista de horarios disponibles, o lista vacía si no está autenticado
     */
    @GetMapping("/horarios-disponibles")
    @ResponseBody
    public List<String> obtenerHorarios(@RequestParam Long empleadoId, 
                                        @RequestParam String fecha,
                                        HttpSession session) {
        if (!SessionUtil.estaAutenticado(session)) {
            return Collections.emptyList();
        }

        return citaService.obtenerHorariosDisponibles(empleadoId, fecha);
    }

    /**
     * Crea una nueva cita para el usuario autenticado.
     * 
     * @param servicio_id ID del servicio
     * @param empleado_id ID del empleado
     * @param fecha Fecha en formato YYYY-MM-DD
     * @param hora Hora de la cita
     * @param session Sesión HTTP del usuario
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección al dashboard o formulario de nueva cita en caso de error
     */
    @PostMapping("/nueva")
    public String crearNuevaCita(@RequestParam Long servicio_id,
                                @RequestParam Long empleado_id,
                                @RequestParam String fecha,
                                @RequestParam String hora,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            Long usuarioId = SessionUtil.obtenerUsuarioId(session);
            String usuarioNombre = SessionUtil.obtenerNombre(session);

            if (usuarioId == null) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar el usuario");
                return "redirect:/dashboard";
            }

            String fechaFormateada = convertirFecha(fecha);

            CitaDTO citaDTO = new CitaDTO();
            citaDTO.setUsuario_id(usuarioId);
            citaDTO.setEmpleado_id(empleado_id);
            citaDTO.setServicio_id(servicio_id);
            citaDTO.setFecha(fechaFormateada);
            citaDTO.setHora(hora);
            citaDTO.setEstado("pendiente");

            citaService.crearCita(citaDTO);

            logger.info("Cita creada correctamente para Usuario : {}", usuarioNombre);
            redirectAttributes.addFlashAttribute("success", "¡Cita reservada con éxito!");
            return "redirect:/dashboard";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la cita: " + e.getMessage());
            return "redirect:/citas/nueva";
        }
    }

    /**
     * Convierte una fecha de formato YYYY-MM-DD a DD/MM/YYYY.
     * 
     * @param fecha Fecha en formato YYYY-MM-DD
     * @return Fecha en formato DD/MM/YYYY
     */
    private String convertirFecha(String fecha) {
        String[] partes = fecha.split("-");
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }
}