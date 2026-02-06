package com.estilo360.estilo360.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de seguridad para validar tokens JWT en cada petición HTTP.
 * Este filtro intercepta las solicitudes entrantes, extrae el token JWT
 * del encabezado Authorization y establece la autenticación en el contexto de seguridad.
 * 
 * @version 1.0
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /** Utilidad para operaciones con JWT (validación, extracción de claims, etc.) */
    private final JwtUtil jwtUtil;

    /**
     * Constructor que inyecta la utilidad de JWT.
     * 
     * @param jwtUtil Instancia de JwtUtil para operaciones con tokens
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Método que filtra cada solicitud HTTP entrante.
     * Si el encabezado Authorization contiene un token Bearer válido,
     * se extraen los claims y se establece la autenticación en el contexto de seguridad.
     * 
     * @param request Objeto HttpServletRequest de la petición
     * @param response Objeto HttpServletResponse de la respuesta
     * @param chain Cadena de filtros que permite continuar la ejecución
     * @throws ServletException Excepción lanzada si ocurre un error en el servlet
     * @throws IOException Excepción lanzada si ocurre un error de I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.esTokenValido(token)) {
                Claims claims = jwtUtil.extraerClaims(token);
                String rol = (String) claims.get("rol");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                claims.getSubject(),
                                null,
                                List.of(new SimpleGrantedAuthority(rol))
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}