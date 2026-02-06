package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.CitaDTO;
import com.WebEstilo360.WebEstilo360.dto.ResenaDTO;
import com.WebEstilo360.WebEstilo360.dto.ServicioDTO;
import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import com.WebEstilo360.WebEstilo360.service.ApiUsuarioService;
import com.WebEstilo360.WebEstilo360.service.AuthService;
import com.WebEstilo360.WebEstilo360.service.CitaService;
import com.WebEstilo360.WebEstilo360.service.ResenaService;
import com.WebEstilo360.WebEstilo360.service.ServicioService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para el dashboard y funcionalidades relacionadas
 * con clientes y administradores.
 * 
 * Gestiona:
 * - Dashboard de admin y cliente
 * - Perfil de cliente
 * - Gestión de citas (visualización, cancelación)
 * - Servicios y descarga de PDF
 * - Creación de reseñas
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final CitaService citaService;
    private final ApiUsuarioService usuarioService;
    private final ServicioService servicioService;
    private final AuthService authService;
    private final ResenaService resenaService;

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    /**
     * Redirige al dashboard según el rol del usuario.
     * @param session Sesión HTTP del usuario
     * @param model Modelo para atributos de la vista
     * @return Redirección al dashboard correspondiente
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        String rol = SessionUtil.obtenerRol(session);
        String nombre = SessionUtil.obtenerNombre(session);

        if ("admin".equalsIgnoreCase(rol)) {
            log.info("Acceso a dashboard admin - Usuario: {}", nombre);
            return "redirect:/dashboard-admin";
        } else if ("cliente".equalsIgnoreCase(rol)) {
            log.info("Acceso a dashboard Cliente - Usuario: {}", nombre);
            return "redirect:/dashboard-cliente";
        } else {
            SessionUtil.eliminarToken(session);
            return "redirect:/login";
        }
    }

    /**
     * Dashboard del administrador
     */
    @GetMapping("/dashboard-admin")
    public String dashboardAdmin(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";
        String rol = SessionUtil.obtenerRol(session);
        if (!"admin".equalsIgnoreCase(rol)) {
            log.warn("Acceso denegado a dashboard admin - Rol: {}", rol);
            return "redirect:/dashboard";
        }
        String nombre = SessionUtil.obtenerNombre(session);
        model.addAttribute("nombre_completo", nombre != null ? nombre : "Administrador");
        model.addAttribute("rol", rol);
        return "dashboard-admin";
    }

    /**
     * Dashboard del cliente
     */
    @GetMapping("/dashboard-cliente")
    public String dashboardCliente(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";
        String rol = SessionUtil.obtenerRol(session);
        if (!"cliente".equalsIgnoreCase(rol)) {
            log.warn("Acceso denegado a dashboard cliente - Rol: {}", rol);
            return "redirect:/dashboard";
        }

        try {
            String token = SessionUtil.obtenerToken(session);
            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            model.addAttribute("usuario", usuario);

            CitaDTO proximaCita = citaService.obtenerProximaCita(token);
            model.addAttribute("proximaCita", proximaCita);

            List<CitaDTO> historial = citaService.obtenerHistorial(token);
            model.addAttribute("historial", historial);

            List<CitaDTO> todasCitas = citaService.obtenerMisCitas(token);
            model.addAttribute("todasCitas", todasCitas);

            model.addAttribute("rol", rol);
            return "dashboard-cliente";

        } catch (Exception e) {
            System.err.println("Error en dashboard cliente: " + e.getMessage());
            model.addAttribute("error", "Error al cargar el dashboard");
            model.addAttribute("rol", rol);
            return "dashboard-cliente";
        }
    }

    /**
     * Vista de servicios para clientes
     */
    @GetMapping("/cliente-servicios")
    public String verServicios(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";
        if (!"cliente".equalsIgnoreCase(SessionUtil.obtenerRol(session))) return "redirect:/dashboard";

        try {
            List<ServicioDTO> servicios = servicioService.listarServicios();
            model.addAttribute("servicios", servicios);

            String token = SessionUtil.obtenerToken(session);
            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            model.addAttribute("usuario", usuario);

            return "servicios-cliente";

        } catch (Exception e) {
            System.err.println("Error al cargar servicios: " + e.getMessage());
            model.addAttribute("error", "Error al cargar los servicios");
            return "servicios-cliente";
        }
    }

    /**
     * Vista de perfil de cliente
     */
    @GetMapping("/cliente-perfil")
    public String verPerfil(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";
        if (!"cliente".equalsIgnoreCase(SessionUtil.obtenerRol(session))) return "redirect:/dashboard";

        try {
            String token = SessionUtil.obtenerToken(session);
            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            model.addAttribute("usuario", usuario);
            return "cliente-perfil";

        } catch (Exception e) {
            System.err.println("Error al cargar perfil: " + e.getMessage());
            model.addAttribute("error", "Error al cargar el perfil");
            return "cliente-perfil";
        }
    }

    /**
     * Actualiza los datos del perfil del cliente
     */
    @PostMapping("/cliente-perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute UsuarioDTO usuarioDTO,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            String token = SessionUtil.obtenerToken(session);
            UsuarioDTO usuarioActual = usuarioService.obtenerPerfil(token);

            usuarioDTO.setId_usuario(usuarioActual.getId_usuario());
            usuarioDTO.setCorreo(usuarioActual.getCorreo());
            usuarioDTO.setRol(usuarioActual.getRol());

            usuarioService.actualizarUsuario(usuarioActual.getId_usuario(), usuarioDTO);
            SessionUtil.guardarNombre(session, usuarioDTO.getNombre_completo());

            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
        } catch (Exception e) {
            System.err.println("Error al actualizar perfil: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil");
        }

        return "redirect:/cliente-perfil";
    }

    /**
     * Cambio de contraseña para cliente (envía código de verificación)
     */
    @GetMapping("/cliente-cambiar-password")
    public String cambiarPassword(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            String token = SessionUtil.obtenerToken(session);
            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            authService.solicitarResetPassword(usuario.getCorreo());

            redirectAttributes.addFlashAttribute("success",
                "Código de verificación enviado a " + usuario.getCorreo());

            return "redirect:/verificar-codigo-reset?correo=" + usuario.getCorreo();
        } catch (Exception e) {
            System.err.println("Error al solicitar cambio de contraseña: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                "Error al enviar el código de verificación");
            return "redirect:/cliente-perfil";
        }
    }

    /**
     * Vista de citas del cliente
     */
    @GetMapping("/cliente-citas")
    public String verMisCitas(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";
        if (!"cliente".equalsIgnoreCase(SessionUtil.obtenerRol(session))) return "redirect:/dashboard";

        try {
            String token = SessionUtil.obtenerToken(session);
            Long usuarioId = SessionUtil.obtenerUsuarioId(session);

            List<CitaDTO> citas = citaService.obtenerMisCitas(token);
            model.addAttribute("citas", citas);

            List<ResenaDTO> resenas = resenaService.obtenerResenasPorUsuario(usuarioId);
            model.addAttribute("resenas", resenas);

            UsuarioDTO usuario = usuarioService.obtenerPerfil(token);
            model.addAttribute("usuario", usuario);

            return "citas-cliente";
        } catch (Exception e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
            model.addAttribute("error", "Error al cargar las citas");
            return "citas-cliente";
        }
    }

    /**
     * Cancela una cita del cliente
     */
    @PostMapping("/cliente-citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            String token = SessionUtil.obtenerToken(session);
            citaService.eliminarCita(id, token);
            redirectAttributes.addFlashAttribute("success", "Cita cancelada correctamente");
        } catch (Exception e) {
            System.err.println("Error al cancelar cita: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la cita");
        }

        return "redirect:/cliente-citas";
    }

    /**
     * Crear reseña para un empleado
     */
    @PostMapping("/cliente-citas/crear-resena")
    public String crearResena(@RequestParam Long empleadoId,
                              @RequestParam Integer puntuacion,
                              @RequestParam String comentario,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) return "redirect:/login";

        try {
            Long usuarioId = SessionUtil.obtenerUsuarioId(session);
            if (usuarioId == null) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar el usuario");
                return "redirect:/cliente-citas";
            }

            ResenaDTO resenaDTO = new ResenaDTO();
            resenaDTO.setUsuarioId(usuarioId);
            resenaDTO.setEmpleadoId(empleadoId);
            resenaDTO.setPuntuacion(puntuacion);
            resenaDTO.setComentario(comentario);

            resenaService.crearResena(resenaDTO);
            redirectAttributes.addFlashAttribute("success", "¡Reseña enviada correctamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la reseña: " + e.getMessage());
        }

        return "redirect:/cliente-citas";
    }

    /**
     * Descargar PDF de servicios
     */
    @GetMapping("/cliente-servicios/descargar-pdf")
    public ResponseEntity<byte[]> descargarPdfServicios(HttpSession session) {
        String nombre = SessionUtil.obtenerNombre(session);
        log.info("Descarga de PDF - Usuario: {}", nombre);
        if (!SessionUtil.estaAutenticado(session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            byte[] pdfBytes = servicioService.descargarPdfServicios();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "servicios-estilo360.pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {
            log.error("Error al descargar PDF - Usuario: {}, Error: {}", nombre, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}