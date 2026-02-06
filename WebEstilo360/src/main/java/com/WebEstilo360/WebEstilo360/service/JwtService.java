package com.WebEstilo360.WebEstilo360.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Servicio para operaciones con JWT (JSON Web Tokens).
 * Permite extraer información (claims) de un token, como rol y correo del usuario.
 * Utiliza una clave secreta compartida con la API para la validación del token.
 * 
 * @version 1.0
 */
@Service
public class JwtService {

    /** Clave secreta utilizada para firmar y validar JWT */
    private final String SECRET = "MiSecretoMuyLargoParaJWTQueDebeTenerMuchosCaracteres123!";

    /** Clave generada a partir del secreto para firmar y verificar tokens */
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Extrae los claims de un JWT válido.
     * 
     * @param token Token JWT a analizar
     * @return Claims contenidos en el token
     */
    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene el rol del usuario desde el JWT.
     * 
     * @param token Token JWT
     * @return Rol del usuario
     */
    public String obtenerRol(String token) {
        return (String) extraerClaims(token).get("rol");
    }

    /**
     * Obtiene el correo del usuario (subject) desde el JWT.
     * 
     * @param token Token JWT
     * @return Correo del usuario
     */
    public String obtenerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }
}
