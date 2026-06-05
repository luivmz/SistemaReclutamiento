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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String formularioPostulacion(@PathVariable Long id, Model model) {
        var oferta = ofertaService.buscarActiva(id);
        if (oferta == null) {
            return "redirect:/ofertas";
        }
        model.addAttribute("oferta", oferta);
        model.addAttribute("postulante", new PostulanteDTO());
        return "postulante/postular";
    }

    @PostMapping("/ofertas/{id}/postular")
    public String postular(@PathVariable Long id, @ModelAttribute PostulanteDTO postulante,
                           HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        try {
            // La validacion se repite aqui para devolver el formulario con un mensaje claro antes de llamar al servicio.
            if (postulanteService.yaPostulo(usuarioId, id)) {
                model.addAttribute("oferta", ofertaService.buscarActiva(id));
                model.addAttribute("error", "Ya postulaste a esta oferta.");
                model.addAttribute("postulante", postulante);
                return "postulante/postular";
            }
            postulante.setOfertaId(id);
            postulanteService.postular(postulante, usuarioId);
            return "redirect:/postulante/mis-postulaciones";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            // Ante errores de validacion se reconstruye el modelo que necesita la misma vista JSP.
            model.addAttribute("oferta", ofertaService.buscarActiva(id));
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("postulante", postulante);
            return "postulante/postular";
        }
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

    @PostMapping("/postulante/postulaciones/{id}/cancelar")
    public String cancelar(@PathVariable Long id, HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        try {
            postulanteService.cancelar(id, usuarioId);
            return "redirect:/postulante/mis-postulaciones";
        } catch (IllegalStateException ex) {
            // Si no pudo cancelar, se vuelve a la pantalla de estado manteniendo sus entrevistas cargadas.
            PostulanteDTO postulacion = postulanteService.buscar(id);
            if (postulacion == null || !usuarioId.equals(postulacion.getUsuarioId())) {
                return "redirect:/acceso-denegado";
            }
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("postulacion", postulacion);
            model.addAttribute("entrevistas", entrevistaService.listarPorPostulante(id));
            return "postulante/estado";
        }
    }

    @PostMapping("/postulante/entrevistas/{id}/cancelar")
    public String cancelarEntrevista(@PathVariable Long id, HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        try {
            Long postulanteId = entrevistaService.cancelarEntrevista(id, usuarioId);
            redirectAttributes.addFlashAttribute("mensaje", "Entrevista cancelada correctamente.");
            return "redirect:/postulante/postulaciones/" + postulanteId + "/estado";
        } catch (SecurityException ex) {
            return "redirect:/acceso-denegado";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/postulante/mis-postulaciones";
        }
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
    public String registrarPostulanteAdmin(@ModelAttribute PostulanteDTO postulante, HttpSession session, Model model) {
        try {
            // El mismo formulario sirve para crear una postulacion administrativa o actualizar una existente.
            if (postulante.getId() == null) {
                postulanteService.postular(postulante, postulante.getUsuarioId());
            } else {
                postulanteService.actualizar(postulante, usuarioActual(session));
            }
            return "redirect:/admin/postulantes";
        } catch (IllegalStateException | IllegalArgumentException ex) {
            // Cuando falla, se recargan todas las listas porque la JSP muestra formulario, activos e historial juntos.
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("activos", postulanteService.listarActivos());
            model.addAttribute("historial", postulanteService.listarHistorial());
            model.addAttribute("usuarios", usuarioService.listarPostulantes());
            model.addAttribute("ofertas", ofertaService.listarActivas());
            return "admin/postulantes";
        }
    }

    @GetMapping("/admin/postulantes/aprobar/{id}")
    public String aprobar(@PathVariable Long id, HttpSession session) {
        postulanteService.aprobar(id, usuarioActual(session));
        return "redirect:/admin/postulantes";
    }

    @GetMapping("/admin/postulantes/rechazar/{id}")
    public String rechazar(@PathVariable Long id, HttpSession session) {
        postulanteService.rechazar(id, usuarioActual(session));
        return "redirect:/admin/postulantes";
    }

    @GetMapping("/admin/postulantes/estado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Long id, @PathVariable String estado, HttpSession session) {
        postulanteService.cambiarEstado(id, estado, usuarioActual(session));
        return "redirect:/admin/postulantes";
    }

    private String usuarioActual(HttpSession session) {
        Object nombre = session.getAttribute("nombre");
        if (nombre != null) {
            return nombre.toString();
        }
        Object email = session.getAttribute("email");
        return email == null ? "Administrador" : email.toString();
    }
}
