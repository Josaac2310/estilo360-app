package com.WebEstilo360.WebEstilo360.util;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    /**
     * Guarda el token en la sesión
     */
    public static void guardarToken(HttpSession session, String token) {
        session.setAttribute("token", token);
    }

    /**
     * Obtiene el token de la sesión
     */
    public static String obtenerToken(HttpSession session) {
        return (String) session.getAttribute("token");
    }

    /**
     * Verifica si hay un token en la sesión
     */
    public static boolean tieneToken(HttpSession session) {
        String token = obtenerToken(session);
        return token != null && !token.isEmpty();
    }

    /**
     * Elimina el token de la sesión (logout)
     */
    public static void eliminarToken(HttpSession session) {
        session.removeAttribute("token");
        session.invalidate();
    }

    /**
     * Guarda datos del usuario en sesión
     */
    public static void guardarUsuario(HttpSession session, Long id, String nombre, String rol) {
        session.setAttribute("usuarioId", id);
        session.setAttribute("usuarioNombre", nombre);
        session.setAttribute("usuarioRol", rol);
    }

    /**
     * Verifica si el usuario está autenticado
     */
    public static boolean estaAutenticado(HttpSession session) {
        return session.getAttribute("token") != null;
    }
    
    public static void guardarRol(HttpSession session, String rol) {
        session.setAttribute("usuarioRol", rol);
    }

    /**
     * Obtiene el rol del usuario de la sesión
     */
    public static String obtenerRol(HttpSession session) {
        return (String) session.getAttribute("usuarioRol");
    }
    
    public static void guardarNombre(HttpSession session, String nombre) {
        session.setAttribute("nombre_completo", nombre);
    }

    /**
     * Obtiene el nombre completo del usuario de la sesión
     */
    public static String obtenerNombre(HttpSession session) {
        return (String) session.getAttribute("nombre_completo");
    }
}