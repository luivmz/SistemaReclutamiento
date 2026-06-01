package edu.reclutamiento.talentoacademico.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    // Este interceptor funciona como una seguridad basica del sistema.
    // Se ejecuta antes del controller para revisar si la sesion y el rol permiten entrar a la ruta.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);
        String rol = session == null ? null : (String) session.getAttribute("rol");

        // Si la ruta empieza con /admin, solo un usuario con rol ADMIN puede ingresar.
        if (path.startsWith("/admin") && !"ADMIN".equals(rol)) {
            response.sendRedirect("/login");
            return false;
        }

        // Si la ruta empieza con /postulante, solo un usuario con rol POSTULANTE puede ingresar.
        if (path.startsWith("/postulante") && !"POSTULANTE".equals(rol)) {
            response.sendRedirect("/login");
            return false;
        }

        // Para postular a una oferta se exige sesion activa.
        if (path.matches("/ofertas/\\d+/postular") && rol == null) {
            response.sendRedirect("/login?mensaje=Debes%20iniciar%20sesion%20o%20crear%20una%20cuenta%20para%20postular.");
            return false;
        }

        // Un administrador puede gestionar ofertas, pero no postular como candidato.
        if (path.matches("/ofertas/\\d+/postular") && !"POSTULANTE".equals(rol)) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}
