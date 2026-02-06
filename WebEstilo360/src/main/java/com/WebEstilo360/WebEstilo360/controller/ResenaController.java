package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.EmpleadoDTO;
import com.WebEstilo360.WebEstilo360.dto.ResenaDTO;
import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import com.WebEstilo360.WebEstilo360.service.ApiUsuarioService;
import com.WebEstilo360.WebEstilo360.service.EmpleadoService;
import com.WebEstilo360.WebEstilo360.service.ResenaService;
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

/**
 * Controlador para la gestión de reseñas.
 * 
 * Funcionalidades:
 * - Listar todas las reseñas
 * - Eliminar reseñas
 */
@Controller
@RequestMapping("/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;
    private final ApiUsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    /**
     * Lista todas las reseñas y enriquece la información con nombres de usuarios y empleados.
     * 
     * @param session Sesión HTTP
     * @param model   Modelo para atributos de la vista
     * @return Vista de reseñas
     */
    @GetMapping
    public String listarResenas(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            List<ResenaDTO> resenas = resenaService.listarResenas();
            List<UsuarioDTO> usuarios = usuarioService.listarTodosLosUsuarios();
            List<EmpleadoDTO> empleados = empleadoService.listarEmpleados();

            // Crear mapas para buscar nombres por ID
            Map<Long, String> usuariosMap = new HashMap<>();
            Map<Long, String> empleadosMap = new HashMap<>();

            usuarios.forEach(u -> usuariosMap.put(u.getId_usuario(), u.getNombre_completo()));
            empleados.forEach(e -> empleadosMap.put(e.getId_empleado(), e.getNombre_completo()));

            // Enriquecer las reseñas con nombres
            resenas.forEach(resena -> {
                resena.setNombreUsuario(usuariosMap.get(resena.getUsuarioId()));
                resena.setNombreEmpleado(empleadosMap.get(resena.getEmpleadoId()));
            });

            model.addAttribute("resenas", resenas);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));

            return "resenas";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las reseñas: " + e.getMessage());
            return "resenas";
        }
    }

    /**
     * Elimina una reseña por su ID.
     * 
     * @param id                 ID de la reseña
     * @param session            Sesión HTTP
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a la lista de reseñas
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarResena(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            resenaService.eliminarResena(id);
            redirectAttributes.addFlashAttribute("success", "Reseña eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reseña");
        }

        return "redirect:/resenas";
    }
}