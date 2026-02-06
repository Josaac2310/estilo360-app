package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    /**
     * el /dashboard lo que hace es ver si el usuario esta registrado 
     * lee el rol de la sesion que esta activa y redirige al dashboard en funcion de su rol
     */
	@GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Verificar si está autenticado
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        // Obtener el rol de la sesión
        String rol = SessionUtil.obtenerRol(session);

        // Redirigir según el rol
        if ("admin".equalsIgnoreCase(rol)) {
            return "redirect:/dashboard-admin";
        } else if ("cliente".equalsIgnoreCase(rol)) {
            return "redirect:/dashboard-cliente";
        } else {
            // Si no tiene rol válido, cerrar sesión
            SessionUtil.eliminarToken(session);
            return "redirect:/login";
        }
    }
	
	@GetMapping("/dashboard-admin")
	public String dashboardAdmin(HttpSession session, Model model) {
	    // Verificar autenticación
	    if (!SessionUtil.estaAutenticado(session)) {
	        return "redirect:/login";
	    }

	    // Verificar que sea admin
	    String rol = SessionUtil.obtenerRol(session);
	    if (!"admin".equalsIgnoreCase(rol)) {
	        return "redirect:/dashboard";
	    }

	    // ← AGREGAR: Pasar el nombre al modelo
	    String nombre = SessionUtil.obtenerNombre(session);
	    model.addAttribute("nombre_completo", nombre != null ? nombre : "Administrador");
	    model.addAttribute("rol", rol);
	    
	    return "dashboard-admin";
	}

    @GetMapping("/dashboard-cliente")
    public String dashboardCliente(HttpSession session, Model model) {
        // Verificar autenticación
        if (!SessionUtil.estaAutenticado(session)) {
            return "redirect:/login";
        }

        // Verificar que sea cliente
        String rol = SessionUtil.obtenerRol(session);
        if (!"cliente".equalsIgnoreCase(rol)) {
            return "redirect:/dashboard"; // Redirigir a su dashboard correcto
        }

        model.addAttribute("rol", rol);
        return "dashboard-cliente";
    }

}