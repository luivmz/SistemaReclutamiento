package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.model.Usuario;
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

    @PostMapping("/postular")
    public String postular(@RequestParam Long ofertaId, @ModelAttribute PostulanteDTO postulante, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            postulante.setNombre(usuario.getNombre());
            postulante.setEmail(usuario.getEmail());
        }
        postulante.setOfertaId(ofertaId);
        postulanteService.postular(postulante);
        return "redirect:/postulante/mis-postulaciones";
    }

    @GetMapping("/postulante/mis-postulaciones")
    public String misPostulaciones(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String email = usuario == null ? "" : usuario.getEmail();
        model.addAttribute("postulaciones", postulanteService.listarPorEmail(email));
        return "postulante/mis-postulaciones";
    }

    @GetMapping("/postulante/evaluacion")
    public String evaluacion(Model model) {
        model.addAttribute("ofertas", ofertaService.listarActivas());
        return "postulante/evaluacion";
    }

    @GetMapping("/postulante/historial")
    public String historial(Model model) {
        model.addAttribute("historial", postulanteService.listarHistorial());
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
