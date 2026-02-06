package com.estilo360.estilo360.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estilo360.estilo360.entidades.Usuario;

/**
 * Repositorio JPA para la gestión de entidades {@link Usuario}.
 * 
 * Proporciona métodos de acceso a datos relacionados con los usuarios,
 * incluyendo búsquedas por correo electrónico y por token de verificación.
 * 
 * @version 1.0
 */
@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param correo Correo electrónico del usuario
     * @return Optional que contiene el usuario si existe, o vacío si no se encuentra
     */
    Optional<Usuario> findByCorreo(String correo);
    
    /**
     * Busca un usuario por su token de verificación.
     * 
     * @param token Token de verificación asociado al usuario
     * @return Optional que contiene el usuario si existe, o vacío si no se encuentra
     */
    Optional<Usuario> findByTokenVerificacion(String token);
}
