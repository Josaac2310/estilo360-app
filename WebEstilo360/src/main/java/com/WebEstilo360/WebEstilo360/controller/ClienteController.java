package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.CitaDTO;
import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import com.WebEstilo360.WebEstilo360.service.CitaService;
import com.WebEstilo360.WebEstilo360.service.ApiUsuarioService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

/**
 * Controlador MVC para el dashboard del cliente.
 * 
 * Proporciona endpoints para mostrar el dashboard del cliente, incluyendo
 * su información personal, próxima cita, historial de citas y todas sus citas.
 * @version 1.0
 */
@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    /** Servicio para operaciones relacionadas con citas */
    private final CitaService citaService;

    /** Servicio para obtener información de usuarios */
    private final ApiUsuarioService usuarioService;

    /**
     * Muestra el dashboard del cliente con información del usuario,
     * próxima cita, historial y todas las citas.
     * 
     * @param session Sesión HTTP del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "cliente/dashboard" o redirección a login/admin según el rol
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Verificar si hay sesión activa
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        // Verificar que sea cliente
        String rol = SessionUtil.obtenerRol(session);
        if (!"cliente".equalsIgnoreCase(rol)) {
            return "redirect:/admin/dashboard";
        }

        try {
            String token = SessionUtil.obtenerToken(session);

            // Obtener datos del usuario
            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            model.addAttribute("usuario", usuario);

            // Obtener próxima cita
            CitaDTO proximaCita = citaService.obtenerProximaCita(token);
            model.addAttribute("proximaCita", proximaCita);

            // Obtener historial (últimas 3 citas)
            List<CitaDTO> historial = citaService.obtenerHistorial(token);
            model.addAttribute("historial", historial);

            // Obtener todas las citas del cliente
            List<CitaDTO> todasLasCitas = citaService.obtenerMisCitas(token);
            System.out.println("todasCitas size: " + (todasLasCitas != null ? todasLasCitas.size() : "null"));
            model.addAttribute("todasCitas", todasLasCitas != null ? todasLasCitas : Collections.emptyList());

            return "cliente/dashboard";

        } catch (Exception e) {
            System.err.println("Error en dashboard cliente: " + e.getMessage());
            model.addAttribute("error", "Error al cargar el dashboard");
            return "cliente/dashboard";
        }
    }
}