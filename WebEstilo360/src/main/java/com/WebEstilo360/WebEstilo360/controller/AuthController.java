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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador REST/MVC para la autenticación de usuarios.
 * 
 * Gestiona endpoints relacionados con login, registro, verificación de correo, 
 * recuperación y cambio de contraseña.
 * 
 * Utiliza AuthService para la lógica de negocio y JwtService para el manejo de JWT.
 * También inyecta un RestTemplate para llamadas HTTP a servicios externos.
 * @version 1.0
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    /** Servicio de autenticación y gestión de usuarios */
    private final AuthService authService;

    /** Cliente HTTP para llamadas a servicios externos */
    private final RestTemplate restTemplate; // ← AGREGAR

    /** Servicio para manejo de tokens JWT */
    private final JwtService jwtService;

    /** Logger para seguimiento de eventos de autenticación */
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    /**
     * Página de inicio.
     * Redirige al login o al dashboard si el usuario ya está autenticado.
     * 
     * @param session Sesión HTTP del usuario
     * @return Ruta a redirigir (login o dashboard)
     */
    @GetMapping("/")
    public String inicio(HttpSession session) {
        if (SessionUtil.estaAutenticado(session)) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    /**
     * Muestra el formulario de login.
     * 
     * @param session Sesión HTTP del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "login"
     */
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session, Model model) {
        if (SessionUtil.estaAutenticado(session)) {
            return "redirect:/dashboard";
        }
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "login";
    }

    /**
     * Procesa el login de un usuario.
     * Verifica credenciales, guarda token en sesión y extrae información del JWT.
     * 
     * @param loginRequest Datos del login enviados desde el formulario
     * @param session Sesión HTTP del usuario
     * @param redirectAttributes Atributos para mensajes flash
     * @param model Modelo para pasar atributos a la vista
     * @return Redirección a dashboard en caso de éxito o login en caso de error
     */
    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginRequestDTO loginRequest,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        log.info("Intento de login - Usuario: {}", loginRequest.getCorreo());

        try {
            LoginResponseDTO response = authService.login(loginRequest);

            if (response != null && response.getToken() != null) {
                SessionUtil.guardarToken(session, response.getToken());
                String rol = jwtService.obtenerRol(response.getToken());
                String correo = jwtService.obtenerCorreo(response.getToken());
                log.info("Login exitoso - Usuario: {}, Rol: {}", correo, rol);

                SessionUtil.guardarRol(session, rol);
                SessionUtil.guardarCorreo(session, correo);

                try {
                    UsuarioDTO usuario = authService.obtenerUsuarioPorCorreo(correo);
                    if (usuario != null) {
                        SessionUtil.guardarNombre(session, usuario.getNombre_completo());
                        SessionUtil.guardarUsuarioId(session, usuario.getId_usuario());
                    }
                } catch (Exception e) {
                    System.err.println("Error al obtener datos del usuario: " + e.getMessage());
                }

                redirectAttributes.addFlashAttribute("success", 
                    "¡Bienvenido! Has iniciado sesión correctamente.");
                return "redirect:/dashboard";
            } else {
                log.error("Error en login ", loginRequest.getCorreo());
                model.addAttribute("error", "Error al procesar el login");
                model.addAttribute("loginRequest", loginRequest);
                return "login";
            }

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("loginRequest", loginRequest);
            return "login";
        }
    }

    /**
     * Cierra la sesión del usuario (logout).
     * 
     * @param session Sesión HTTP del usuario
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a la página de login
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String nombre = SessionUtil.obtenerNombre(session);
        log.info("Cierre de sesión - Usuario: {}", nombre);
        SessionUtil.eliminarToken(session);
        redirectAttributes.addFlashAttribute("success", "Has cerrado sesión correctamente.");
        return "redirect:/login";
    }

    /**
     * Muestra el formulario de registro de usuario.
     * 
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "registro"
     */
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "registro";
    }

    /**
     * Procesa el registro de un nuevo usuario.
     * Fuerza el rol "cliente" y envía mensaje de éxito o error.
     * 
     * @param usuarioDTO Datos del usuario enviados desde el formulario
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a registro exitoso o registro en caso de error
     */
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute UsuarioDTO usuarioDTO,
                                   RedirectAttributes redirectAttributes) {
        try {
            usuarioDTO.setRol("cliente");
            authService.register(usuarioDTO);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "¡Cuenta creada! Te hemos enviado un código de verificación a tu correo."
            );

            return "redirect:/registro-exitoso?correo=" + usuarioDTO.getCorreo();

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }

    /**
     * Muestra la página de registro exitoso con el correo del usuario.
     * 
     * @param correo Correo electrónico del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "registro-exitoso"
     */
    @GetMapping("/registro-exitoso")
    public String registroExitoso(@RequestParam String correo, Model model) {
        model.addAttribute("correo", correo);
        return "registro-exitoso";
    }

    /**
     * Verifica el código enviado al correo del usuario para completar el registro.
     * 
     * @param correo Correo electrónico del usuario
     * @param codigo Código de verificación enviado al correo
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a login si es correcto, o a registro-exitoso si falla
     */
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

    /**
     * Muestra el formulario de "Olvidé mi contraseña".
     * 
     * @return Nombre de la vista "olvide-password"
     */
    @GetMapping("/olvide-password")
    public String mostrarOlvidePassword() {
        return "olvide-password";
    }

    /**
     * Procesa la solicitud de recuperación de contraseña enviando un código al correo.
     * 
     * @param correo Correo electrónico del usuario
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a verificación de código o al formulario de olvide-password en caso de error
     */
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

    /**
     * Muestra el formulario para ingresar el código de recuperación.
     * 
     * @param correo Correo electrónico del usuario
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "verificar-codigo-reset"
     */
    @GetMapping("/verificar-codigo-reset")
    public String mostrarVerificarCodigoReset(@RequestParam String correo, Model model) {
        model.addAttribute("correo", correo);
        return "verificar-codigo-reset";
    }

    /**
     * Procesa la verificación del código de recuperación de contraseña.
     * 
     * @param correo Correo electrónico del usuario
     * @param codigo Código de recuperación
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a nueva contraseña si es correcto, o al formulario de verificación si falla
     */
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

    /**
     * Muestra el formulario para establecer una nueva contraseña.
     * 
     * @param correo Correo electrónico del usuario
     * @param codigo Código de verificación
     * @param model Modelo para pasar atributos a la vista
     * @return Nombre de la vista "nueva-password"
     */
    @GetMapping("/nueva-password")
    public String mostrarNuevaPassword(@RequestParam String correo,
                                       @RequestParam String codigo,
                                       Model model) {
        model.addAttribute("correo", correo);
        model.addAttribute("codigo", codigo);
        return "nueva-password";
    }

    /**
     * Procesa el cambio de contraseña del usuario.
     * Valida coincidencia y longitud mínima de la nueva contraseña.
     * 
     * @param correo Correo electrónico del usuario
     * @param codigo Código de verificación
     * @param nuevaPassword Nueva contraseña
     * @param confirmarPassword Confirmación de la nueva contraseña
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirección a login si se actualiza correctamente o al formulario de nueva contraseña si hay errores
     */
    @PostMapping("/nueva-password")
    public String procesarNuevaPassword(@RequestParam String correo,
                                        @RequestParam String codigo,
                                        @RequestParam String nuevaPassword,
                                        @RequestParam String confirmarPassword,
                                        RedirectAttributes redirectAttributes) {
        try {
            if (!nuevaPassword.equals(confirmarPassword)) {
                redirectAttributes.addFlashAttribute("error",
                    "Las contraseñas no coinciden.");
                return "redirect:/nueva-password?correo=" + correo + "&codigo=" + codigo;
            }

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