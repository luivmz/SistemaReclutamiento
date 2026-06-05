package edu.reclutamiento.talentoacademico.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    // Se ejecuta antes del controller y funciona como seguridad basica del sistema.
    // Valida que exista una sesion y que el usuario tenga el rol permitido para la ruta.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);
        // getSession(false) evita crear una sesion nueva solo para comprobar si el usuario inicio sesion.
        String rol = session == null ? null : (String) session.getAttribute("rol");

        // Las rutas /admin/** solo pueden ser usadas por usuarios con rol ADMIN.
        if (path.startsWith("/admin") && !"ADMIN".equals(rol)) {
            response.sendRedirect("/login");
            return false;
        }

        // Las rutas /postulante/** solo pueden ser usadas por usuarios con rol POSTULANTE.
        if (path.startsWith("/postulante") && !"POSTULANTE".equals(rol)) {
            response.sendRedirect("/login");
            return false;
        }

        // Para postular a una oferta debe existir una sesion con un rol registrado.
        if (path.matches("/ofertas/\\d+/postular") && rol == null) {
            response.sendRedirect("/login?mensaje=Debes%20iniciar%20sesion%20o%20crear%20una%20cuenta%20para%20postular.");
            return false;
        }

        // Aunque tenga sesion, solo el rol POSTULANTE puede enviar una postulacion.
        if (path.matches("/ofertas/\\d+/postular") && !"POSTULANTE".equals(rol)) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}
