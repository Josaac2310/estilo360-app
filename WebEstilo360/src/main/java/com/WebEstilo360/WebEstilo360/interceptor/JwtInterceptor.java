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

/**
 * Interceptor para agregar automáticamente el token JWT a las solicitudes HTTP salientes.
 * Este componente implementa ClientHttpRequestInterceptor y se utiliza para interceptar
 * las llamadas HTTP realizadas por RestTemplate u otros clientes HTTP de Spring.
 * Obtiene el token almacenado en la sesión actual y lo añade al header "Authorization".
 * 
 * @version 1.0
 */
@Component
public class JwtInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Intercepta la solicitud HTTP antes de enviarla.
     * Si existe un token JWT en la sesión, lo agrega al header "Authorization".
     * 
     * @param request La solicitud HTTP que será enviada
     * @param body El cuerpo de la solicitud HTTP
     * @param execution Objeto que permite continuar la ejecución de la solicitud
     * @return La respuesta HTTP recibida luego de ejecutar la solicitud
     */
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