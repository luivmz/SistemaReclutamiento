package edu.reclutamiento.talentoacademico.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);
        String rol = session == null ? null : (String) session.getAttribute("rol");

        if (path.startsWith("/admin") && !"ADMIN".equals(rol)) {
            response.sendRedirect("/login");
            return false;
        }

        if (path.startsWith("/postulante") && !"POSTULANTE".equals(rol)) {
            response.sendRedirect("/login");
            return false;
        }

        if (path.matches("/ofertas/\\d+/postular") && rol == null) {
            response.sendRedirect("/login?mensaje=Debes%20iniciar%20sesion%20o%20crear%20una%20cuenta%20para%20postular.");
            return false;
        }

        if (path.matches("/ofertas/\\d+/postular") && !"POSTULANTE".equals(rol)) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}
