package com.WebEstilo360.WebEstilo360.util;

import jakarta.servlet.http.HttpSession;

/**
 * Utilidad para manejar la sesión del usuario.
 * Proporciona métodos estáticos para guardar, obtener y eliminar
 * información de sesión, como token, datos de usuario y rol.
 * 
 * Esta clase facilita el manejo de autenticación y autorización
 * en la capa de presentación.
 * 
 * @version 1.0
 */
public class SessionUtil {

    /**
     * Guarda el token de autenticación en la sesión.
     * 
     * @param session Sesión HTTP
     * @param token Token JWT a guardar
     */
    public static void guardarToken(HttpSession session, String token) {
        session.setAttribute("token", token);
    }

    /**
     * Obtiene el token de autenticación almacenado en la sesión.
     * 
     * @param session Sesión HTTP
     * @return Token JWT, o null si no existe
     */
    public static String obtenerToken(HttpSession session) {
        return (String) session.getAttribute("token");
    }

    /**
     * Verifica si hay un token válido en la sesión.
     * 
     * @param session Sesión HTTP
     * @return true si existe un token no vacío, false en caso contrario
     */
    public static boolean tieneToken(HttpSession session) {
        String token = obtenerToken(session);
        return token != null && !token.isEmpty();
    }

    /**
     * Elimina el token de la sesión y finaliza la sesión (logout).
     * 
     * @param session Sesión HTTP
     */
    public static void eliminarToken(HttpSession session) {
        session.removeAttribute("token");
        session.invalidate();
    }

    /**
     * Guarda información básica del usuario en la sesión.
     * 
     * @param session Sesión HTTP
     * @param id ID del usuario
     * @param nombre Nombre completo del usuario
     * @param rol Rol del usuario
     */
    public static void guardarUsuario(HttpSession session, Long id, String nombre, String rol) {
        session.setAttribute("usuarioId", id);
        session.setAttribute("usuarioNombre", nombre);
        session.setAttribute("usuarioRol", rol);
    }

    /**
     * Verifica si el usuario está autenticado.
     * 
     * @param session Sesión HTTP
     * @return true si existe un token en sesión, false en caso contrario
     */
    public static boolean estaAutenticado(HttpSession session) {
        return session.getAttribute("token") != null;
    }

    /**
     * Guarda el rol del usuario en la sesión.
     * 
     * @param session Sesión HTTP
     * @param rol Rol del usuario
     */
    public static void guardarRol(HttpSession session, String rol) {
        session.setAttribute("usuarioRol", rol);
    }

    /**
     * Obtiene el rol del usuario almacenado en la sesión.
     * 
     * @param session Sesión HTTP
     * @return Rol del usuario, o null si no existe
     */
    public static String obtenerRol(HttpSession session) {
        return (String) session.getAttribute("usuarioRol");
    }

    /**
     * Guarda el nombre completo del usuario en la sesión.
     * 
     * @param session Sesión HTTP
     * @param nombre Nombre completo del usuario
     */
    public static void guardarNombre(HttpSession session, String nombre) {
        session.setAttribute("nombre_completo", nombre);
    }

    /**
     * Obtiene el nombre completo del usuario almacenado en la sesión.
     * 
     * @param session Sesión HTTP
     * @return Nombre completo del usuario, o null si no existe
     */
    public static String obtenerNombre(HttpSession session) {
        return (String) session.getAttribute("nombre_completo");
    }

    /**
     * Guarda el correo electrónico del usuario en la sesión.
     * 
     * @param session Sesión HTTP
     * @param correo Correo del usuario
     */
    public static void guardarCorreo(HttpSession session, String correo) {
        session.setAttribute("correo", correo);
    }

    /**
     * Obtiene el correo electrónico del usuario almacenado en la sesión.
     * 
     * @param session Sesión HTTP
     * @return Correo del usuario, o null si no existe
     */
    public static String obtenerCorreo(HttpSession session) {
        return (String) session.getAttribute("correo");
    }

    /**
     * Guarda el ID del usuario en la sesión.
     * 
     * @param session Sesión HTTP
     * @param id ID del usuario
     */
    public static void guardarUsuarioId(HttpSession session, Long id) {
        session.setAttribute("usuarioId", id);
    }

    /**
     * Obtiene el ID del usuario almacenado en la sesión.
     * 
     * @param session Sesión HTTP
     * @return ID del usuario, o null si no existe
     */
    public static Long obtenerUsuarioId(HttpSession session) {
        return (Long) session.getAttribute("usuarioId");
    }
}