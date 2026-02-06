package com.WebEstilo360.WebEstilo360.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    // ← LA MISMA CLAVE QUE EN EL API
    private final String SECRET = "MiSecretoMuyLargoParaJWTQueDebeTenerMuchosCaracteres123!";
    
    // ← CAMBIAR: Generar key desde el SECRET
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String obtenerRol(String token) {
        return (String) extraerClaims(token).get("rol");
    }

    public String obtenerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }
}