package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import com.WebEstilo360.WebEstilo360.service.ApiUsuarioService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final ApiUsuarioService apiUsuarioService;

    @GetMapping
    public String listarUsuarios(HttpSession session, Model model) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            List<UsuarioDTO> usuarios = apiUsuarioService.listarTodosLosUsuarios();
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));

            return "usuarios";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los usuarios: " + e.getMessage());
            return "usuarios";
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
            UsuarioDTO usuario = apiUsuarioService.obtenerUsuarioPorId(id);

            if (usuario == null) {
                return "redirect:/usuarios";
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("nombre", SessionUtil.obtenerNombre(session));

            return "editar-usuario";

        } catch (Exception e) {
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id,
                                @ModelAttribute UsuarioDTO usuarioDTO,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            usuarioDTO.setId_usuario(id);
            // La contraseña solo se actualiza si se proporciona
            apiUsuarioService.actualizarUsuario(id, usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        try {
            apiUsuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario");
        }

        return "redirect:/usuarios";
    }
}