package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.service.OfertaService;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final PostulanteService postulanteService;
    private final OfertaService ofertaService;

    public DashboardController(PostulanteService postulanteService, OfertaService ofertaService) {
        this.postulanteService = postulanteService;
        this.ofertaService = ofertaService;
    }

    @GetMapping("/postulante/dashboard")
    public String postulanteDashboard(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        model.addAttribute("nombre", session.getAttribute("nombre"));
        model.addAttribute("ofertas", ofertaService.listarActivas().size());
        model.addAttribute("total", postulanteService.contarPorUsuario(usuarioId));
        model.addAttribute("evaluacion", postulanteService.contarPorUsuarioYEstado(usuarioId, "EN_EVALUACION"));
        model.addAttribute("aprobadas", postulanteService.contarPorUsuarioYEstado(usuarioId, "APROBADO"));
        model.addAttribute("rechazadas", postulanteService.contarPorUsuarioYEstado(usuarioId, "RECHAZADO"));
        return "postulante/dashboard";
    }
}
