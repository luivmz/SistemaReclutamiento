package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.service.EntrevistaService;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import edu.reclutamiento.talentoacademico.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostulanteController {
    private final PostulanteService postulanteService;
    private final OfertaService ofertaService;
    private final UsuarioService usuarioService;
    private final EntrevistaService entrevistaService;

    public PostulanteController(PostulanteService postulanteService, OfertaService ofertaService,
                                UsuarioService usuarioService, EntrevistaService entrevistaService) {
        this.postulanteService = postulanteService;
        this.ofertaService = ofertaService;
        this.usuarioService = usuarioService;
        this.entrevistaService = entrevistaService;
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
        model.addAttribute("entrevistas", entrevistaService.listarPorPostulante(id));
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
        model.addAttribute("usuarios", usuarioService.listarPostulantes());
        model.addAttribute("ofertas", ofertaService.listarActivas());
        model.addAttribute("postulante", new PostulanteDTO());
        return "admin/postulantes";
    }

    @GetMapping("/admin/postulantes/editar/{id}")
    public String editarPostulanteAdmin(@PathVariable Long id, Model model) {
        model.addAttribute("activos", postulanteService.listarActivos());
        model.addAttribute("historial", postulanteService.listarHistorial());
        model.addAttribute("usuarios", usuarioService.listarPostulantes());
        model.addAttribute("ofertas", ofertaService.listarActivas());
        model.addAttribute("postulante", postulanteService.buscar(id));
        return "admin/postulantes";
    }

    @PostMapping("/admin/postulantes")
    public String registrarPostulanteAdmin(@ModelAttribute PostulanteDTO postulante, Model model) {
        try {
            if (postulante.getId() == null) {
                postulanteService.postular(postulante, postulante.getUsuarioId());
            } else {
                postulanteService.actualizar(postulante);
            }
            return "redirect:/admin/postulantes";
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("activos", postulanteService.listarActivos());
            model.addAttribute("historial", postulanteService.listarHistorial());
            model.addAttribute("usuarios", usuarioService.listarPostulantes());
            model.addAttribute("ofertas", ofertaService.listarActivas());
            return "admin/postulantes";
        }
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

    @GetMapping("/admin/postulantes/estado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Long id, @PathVariable String estado) {
        postulanteService.cambiarEstado(id, estado);
        return "redirect:/admin/postulantes";
    }
}
