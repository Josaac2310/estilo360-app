package com.WebEstilo360.WebEstilo360.controller;

import com.WebEstilo360.WebEstilo360.dto.LoginRequestDTO;
import com.WebEstilo360.WebEstilo360.dto.LoginResponseDTO;
import com.WebEstilo360.WebEstilo360.dto.UsuarioDTO;
import com.WebEstilo360.WebEstilo360.service.AuthService;
import com.WebEstilo360.WebEstilo360.service.JwtService;
import com.WebEstilo360.WebEstilo360.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RestTemplate restTemplate; // ← AGREGAR
    private final JwtService jwtService;
    
    
    /**
     * Página de inicio - redirige al login
     */
    @GetMapping("/")
    public String inicio(HttpSession session) {
        // Si ya está autenticado, redirigir al dashboard
        if (SessionUtil.estaAutenticado(session)) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    /**
     * Muestra el formulario de login
     */
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session, Model model) {
        // Si ya está autenticado, redirigir al dashboard
        if (SessionUtil.estaAutenticado(session)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "login";
    }

    /**
     * Procesa el login
     */
    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginRequestDTO loginRequest,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            // Llamar a la API para hacer login
            LoginResponseDTO response = authService.login(loginRequest);

            if (response != null && response.getToken() != null) {
                // Guardar el token en la sesión
                SessionUtil.guardarToken(session, response.getToken());

                // Extraer datos del JWT
                String rol = jwtService.obtenerRol(response.getToken());
                String correo = jwtService.obtenerCorreo(response.getToken());
                
                SessionUtil.guardarRol(session, rol);

                // ← AGREGAR: Obtener datos completos del usuario desde el API
                try {
                    UsuarioDTO usuario = authService.obtenerUsuarioPorCorreo(correo);
                    if (usuario != null) {
                        SessionUtil.guardarNombre(session, usuario.getNombre_completo());
                    }
                } catch (Exception e) {
                    System.err.println("Error al obtener datos del usuario: " + e.getMessage());
                }

                // Mensaje de éxito
                redirectAttributes.addFlashAttribute("success", 
                    "¡Bienvenido! Has iniciado sesión correctamente.");

                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Error al procesar el login");
                model.addAttribute("loginRequest", loginRequest);
                return "login";
            }

        } catch (RuntimeException e) {
            // Mostrar el error al usuario
            model.addAttribute("error", e.getMessage());
            model.addAttribute("loginRequest", loginRequest);
            return "login";
        }
    }

    /**
     * Cierra la sesión (logout)
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        SessionUtil.eliminarToken(session);
        redirectAttributes.addFlashAttribute("success", "Has cerrado sesión correctamente.");
        return "redirect:/login";
    }
    
    
    
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute UsuarioDTO usuarioDTO,
                                   RedirectAttributes redirectAttributes) {

        try {
            // Seguridad: forzar rol cliente
            usuarioDTO.setRol("cliente");

            authService.register(usuarioDTO);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "¡Cuenta creada! Te hemos enviado un código de verificación a tu correo."
            );

            // ← CAMBIO: Pasar el correo como parámetro
            return "redirect:/registro-exitoso?correo=" + usuarioDTO.getCorreo();

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/registro-exitoso")
    public String registroExitoso(@RequestParam String correo, Model model) {
        model.addAttribute("correo", correo);
        return "registro-exitoso";
    }

    @PostMapping("/verificar-codigo")
    public String verificarCodigo(@RequestParam String correo,
                                  @RequestParam String codigo,
                                  RedirectAttributes redirectAttributes) {
        try {
            boolean verificado = authService.verificarCodigo(correo, codigo);
            
            if (verificado) {
                redirectAttributes.addFlashAttribute("success", 
                    "¡Email verificado correctamente! Ya puedes iniciar sesión.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "Código inválido. Verifica e intenta nuevamente.");
                return "redirect:/registro-exitoso?correo=" + correo;
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al verificar el código: " + e.getMessage());
            return "redirect:/registro-exitoso?correo=" + correo;
        }
    }
    
    //resear contraseña 
    @GetMapping("/olvide-password")
    public String mostrarOlvidePassword() {
        return "olvide-password";
    }

    // Procesar solicitud de reset
    @PostMapping("/olvide-password")
    public String procesarOlvidePassword(@RequestParam String correo,
                                         RedirectAttributes redirectAttributes) {
        try {
            authService.solicitarResetPassword(correo);
            
            redirectAttributes.addFlashAttribute("success",
                "Te hemos enviado un código de recuperación a tu correo.");
            
            return "redirect:/verificar-codigo-reset?correo=" + correo;
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "No existe una cuenta con ese correo electrónico.");
            return "redirect:/olvide-password";
        }
    }

    // Mostrar formulario para ingresar código
    @GetMapping("/verificar-codigo-reset")
    public String mostrarVerificarCodigoReset(@RequestParam String correo, Model model) {
        model.addAttribute("correo", correo);
        return "verificar-codigo-reset";
    }

    // Procesar verificación de código
    @PostMapping("/verificar-codigo-reset")
    public String procesarVerificarCodigoReset(@RequestParam String correo,
                                               @RequestParam String codigo,
                                               RedirectAttributes redirectAttributes) {
        try {
            boolean verificado = authService.verificarCodigoReset(correo, codigo);
            
            if (verificado) {
                return "redirect:/nueva-password?correo=" + correo + "&codigo=" + codigo;
            } else {
                redirectAttributes.addFlashAttribute("error",
                    "Código inválido. Verifica e intenta nuevamente.");
                return "redirect:/verificar-codigo-reset?correo=" + correo;
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/verificar-codigo-reset?correo=" + correo;
        }
    }

    // Mostrar formulario para nueva contraseña
    @GetMapping("/nueva-password")
    public String mostrarNuevaPassword(@RequestParam String correo,
                                       @RequestParam String codigo,
                                       Model model) {
        model.addAttribute("correo", correo);
        model.addAttribute("codigo", codigo);
        return "nueva-password";
    }

    // Procesar cambio de contraseña
    @PostMapping("/nueva-password")
    public String procesarNuevaPassword(@RequestParam String correo,
                                        @RequestParam String codigo,
                                        @RequestParam String nuevaPassword,
                                        @RequestParam String confirmarPassword,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Validar que las contraseñas coincidan
            if (!nuevaPassword.equals(confirmarPassword)) {
                redirectAttributes.addFlashAttribute("error",
                    "Las contraseñas no coinciden.");
                return "redirect:/nueva-password?correo=" + correo + "&codigo=" + codigo;
            }

            // Validar longitud mínima
            if (nuevaPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error",
                    "La contraseña debe tener al menos 6 caracteres.");
                return "redirect:/nueva-password?correo=" + correo + "&codigo=" + codigo;
            }

            authService.resetearPassword(correo, codigo, nuevaPassword);
            
            redirectAttributes.addFlashAttribute("success",
                "Contraseña actualizada correctamente. Ya puedes iniciar sesión.");
            
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "Error al cambiar la contraseña: " + e.getMessage());
            return "redirect:/nueva-password?correo=" + correo + "&codigo=" + codigo;
        }
    }
}
