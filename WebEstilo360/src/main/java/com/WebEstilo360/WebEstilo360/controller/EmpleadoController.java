package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.EmpleadoDTO;
import com.WebEstilo360.WebEstilo360.service.EmpleadoService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para la gestión de empleados.
 * 
 * Funcionalidades:
 * - Listar empleados
 * - Crear nuevo empleado
 * - Editar empleado existente
 * - Eliminar empleado
 */
@Controller
@RequestMapping("/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    /**
     * Lista todos los empleados.
     * @param session Sesión HTTP
     * @param model Modelo para atributos de la vista
     * @return Vista de empleados
     */
    @GetMapping
    public String listarEmpleados(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            List<EmpleadoDTO> empleados = empleadoService.listarEmpleados();
            model.addAttribute("empleados", empleados);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));
            return "empleados";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los empleados: " + e.getMessage());
            return "empleados";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo empleado.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        model.addAttribute("empleado", new EmpleadoDTO());
        model.addAttribute("nombre", SessionUtil.obtenerNombre(session));
        return "crear-empleado";
    }

    /**
     * Crea un nuevo empleado.
     */
    @PostMapping("/nuevo")
    public String crearEmpleado(@ModelAttribute EmpleadoDTO empleadoDTO,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            empleadoService.crearEmpleado(empleadoDTO);
            redirectAttributes.addFlashAttribute("success", "Empleado creado correctamente");
            return "redirect:/empleados";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el empleado: " + e.getMessage());
            return "redirect:/empleados/nuevo";
        }
    }

    /**
     * Muestra el formulario de edición de un empleado existente.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          HttpSession session,
                                          Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            EmpleadoDTO empleado = empleadoService.obtenerEmpleadoPorId(id);
            if (empleado == null) return "redirect:/empleados";

            model.addAttribute("empleado", empleado);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));
            return "editar-empleado";
        } catch (Exception e) {
            return "redirect:/empleados";
        }
    }

    /**
     * Actualiza los datos de un empleado.
     */
    @PostMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Long id,
                                 @ModelAttribute EmpleadoDTO empleadoDTO,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            empleadoDTO.setId_empleado(id);
            empleadoService.actualizarEmpleado(id, empleadoDTO);
            redirectAttributes.addFlashAttribute("success", "Empleado actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el empleado: " + e.getMessage());
        }

        return "redirect:/empleados";
    }

    /**
     * Elimina un empleado.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            empleadoService.eliminarEmpleado(id);
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el empleado");
        }

        return "redirect:/empleados";
    }
}