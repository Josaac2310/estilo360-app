package com.WebEstilo360.WebEstilo360.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
public class JwtInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, 
                                       ClientHttpRequestExecution execution) throws IOException {
        
        // Obtener la sesión actual
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            
            if (session != null) {
                String token = (String) session.getAttribute("token");
                
                if (token != null && !token.isEmpty()) {
                    // Agregar el token al header Authorization
                    request.getHeaders().add("Authorization", "Bearer " + token);
                }
            }
        }
        
        return execution.execute(request, body);
    }
}
