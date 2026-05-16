package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostulanteController {
    private final PostulanteService postulanteService;
    private final OfertaService ofertaService;

    public PostulanteController(PostulanteService postulanteService, OfertaService ofertaService) {
        this.postulanteService = postulanteService;
        this.ofertaService = ofertaService;
    }

    @GetMapping("/ofertas/{id}/postular")
    public String formularioPostulacion(@PathVariable Long id, Model model, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/acceso-denegado";
        }
        model.addAttribute("oferta", ofertaService.buscar(id));
        model.addAttribute("postulante", new PostulanteDTO());
        return "postulante/postular";
    }

    @PostMapping("/ofertas/{id}/postular")
    public String postular(@PathVariable Long id, @ModelAttribute PostulanteDTO postulante,
                           HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if ("ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/acceso-denegado";
        }
        if (postulanteService.yaPostulo(usuarioId, id)) {
            model.addAttribute("oferta", ofertaService.buscar(id));
            model.addAttribute("error", "Ya postulaste a esta oferta.");
            return "postulante/postular";
        }
        postulante.setOfertaId(id);
        postulanteService.postular(postulante, usuarioId);
        return "redirect:/postulante/mis-postulaciones";
    }

    @GetMapping("/postulante/mis-postulaciones")
    public String misPostulaciones(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        model.addAttribute("postulaciones", postulanteService.listarPorUsuario(usuarioId));
        return "postulante/mis-postulaciones";
    }

    @GetMapping("/postulante/postulaciones/{id}/estado")
    public String estado(@PathVariable Long id, HttpSession session, Model model) {
        PostulanteDTO postulacion = postulanteService.buscar(id);
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (postulacion == null || !usuarioId.equals(postulacion.getUsuarioId())) {
            return "redirect:/acceso-denegado";
        }
        model.addAttribute("postulacion", postulacion);
        return "postulante/estado";
    }

    @GetMapping("/postulante/historial")
    public String historial(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        model.addAttribute("historial", postulanteService.listarPorUsuario(usuarioId));
        return "postulante/historial";
    }

    @GetMapping("/admin/postulantes")
    public String adminPostulantes(Model model) {
        model.addAttribute("activos", postulanteService.listarActivos());
        model.addAttribute("historial", postulanteService.listarHistorial());
        return "admin/postulantes";
    }

    @GetMapping("/admin/postulantes/aprobar/{id}")
    public String aprobar(@PathVariable Long id) {
        postulanteService.aprobar(id);
        return "redirect:/admin/postulantes";
    }

    @GetMapping("/admin/postulantes/rechazar/{id}")
    public String rechazar(@PathVariable Long id) {
        postulanteService.rechazar(id);
        return "redirect:/admin/postulantes";
    }

    @GetMapping("/admin/postulantes/finalizar/{id}")
    public String finalizar(@PathVariable Long id) {
        postulanteService.finalizar(id);
        return "redirect:/admin/postulantes";
    }
}
