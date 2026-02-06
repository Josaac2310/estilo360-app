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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;
    private final ApiUsuarioService usuarioService;
    private final EmpleadoService empleadoService;
    private final ServicioService servicioService;

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

            // Cargar listas para los selectores
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
            
            // Mantener el estado y observaciones existentes
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
}