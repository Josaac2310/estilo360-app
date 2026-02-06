package com.estilo360.estilo360.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Utilidad para generar, validar y extraer información de tokens JWT.
 * Proporciona métodos para crear tokens firmados, extraer claims
 * y verificar la validez de los tokens.
 * 
 * @version 1.0
 */
@Component
public class JwtUtil {
    
    /** Clave secreta fija para firmar los JWT (mínimo 256 bits / 32 caracteres) */
    private final String SECRET = "MiSecretoMuyLargoParaJWTQueDebeTenerMuchosCaracteres123!";
    
    /** Duración del token en milisegundos (1 hora) */
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hora
    
    /** Clave generada a partir del secreto para firmar los JWT */
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Genera un token JWT con el correo y rol del usuario.
     * 
     * @param correo Correo del usuario (subject del token)
     * @param rol Rol del usuario (claim "rol")
     * @return Token JWT firmado como String
     */
    public String generarToken(String correo, String rol) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * Extrae los claims (información) de un token JWT.
     * 
     * @param token Token JWT a analizar
     * @return Claims contenidos en el token
     * @throws JwtException Si el token no es válido o ha sido manipulado
     */
    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si un token JWT es válido.
     * 
     * @param token Token JWT a verificar
     * @return true si el token es válido, false si es inválido o expirado
     */
    public boolean esTokenValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obtiene el correo (subject) de un token JWT.
     * 
     * @param token Token JWT
     * @return Correo del usuario contenido en el token
     */
    public String obtenerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }

    /**
     * Obtiene el rol del usuario a partir de un token JWT.
     * 
     * @param token Token JWT
     * @return Rol del usuario contenido en el token
     */
    public String obtenerRol(String token) {
        return (String) extraerClaims(token).get("rol");
    }
}