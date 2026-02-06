package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.ServicioDTO;
import com.WebEstilo360.WebEstilo360.service.ServicioService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para la gestión de servicios.
 * 
 * Funcionalidades:
 * - Listar servicios
 * - Crear, editar y eliminar servicios
 */
@Controller
@RequestMapping("/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    /**
     * Lista todos los servicios.
     */
    @GetMapping
    public String listarServicios(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            List<ServicioDTO> servicios = servicioService.listarServicios();
            model.addAttribute("servicios", servicios);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));
            return "servicios";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los servicios: " + e.getMessage());
            return "servicios";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo servicio.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        model.addAttribute("servicio", new ServicioDTO());
        model.addAttribute("nombre", SessionUtil.obtenerNombre(session));
        return "crear-servicio";
    }

    /**
     * Crea un nuevo servicio.
     */
    @PostMapping("/nuevo")
    public String crearServicio(@ModelAttribute ServicioDTO servicioDTO,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            servicioService.crearServicio(servicioDTO);
            redirectAttributes.addFlashAttribute("success", "Servicio creado correctamente");
            return "redirect:/servicios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el servicio: " + e.getMessage());
            return "redirect:/servicios/nuevo";
        }
    }

    /**
     * Muestra el formulario para editar un servicio existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          HttpSession session,
                                          Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            ServicioDTO servicio = servicioService.obtenerServicioPorId(id);
            if (servicio == null) return "redirect:/servicios";

            model.addAttribute("servicio", servicio);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));
            return "editar-servicio";
        } catch (Exception e) {
            return "redirect:/servicios";
        }
    }

    /**
     * Actualiza un servicio existente.
     */
    @PostMapping("/editar/{id}")
    public String editarServicio(@PathVariable Long id,
                                 @ModelAttribute ServicioDTO servicioDTO,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            servicioDTO.setId_servicio(id);
            servicioService.actualizarServicio(id, servicioDTO);
            redirectAttributes.addFlashAttribute("success", "Servicio actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el servicio: " + e.getMessage());
        }
        return "redirect:/servicios";
    }

    /**
     * Elimina un servicio por su ID.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            servicioService.eliminarServicio(id);
            redirectAttributes.addFlashAttribute("success", "Servicio eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el servicio");
        }
        return "redirect:/servicios";
    }
}