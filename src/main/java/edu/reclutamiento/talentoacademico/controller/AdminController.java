package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.dto.PreguntaDTO;
import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    private final UsuarioService usuarioService;
    private final AreaService areaService;
    private final OfertaService ofertaService;
    private final PostulanteService postulanteService;
    private final EntrevistaService entrevistaService;
    private final PreguntaService preguntaService;

    public AdminController(UsuarioService usuarioService, AreaService areaService, OfertaService ofertaService,
                           PostulanteService postulanteService, EntrevistaService entrevistaService, PreguntaService preguntaService) {
        this.usuarioService = usuarioService;
        this.areaService = areaService;
        this.ofertaService = ofertaService;
        this.postulanteService = postulanteService;
        this.entrevistaService = entrevistaService;
        this.preguntaService = preguntaService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("usuarios", usuarioService.listar().size());
        model.addAttribute("ofertas", ofertaService.listar().size());
        model.addAttribute("activos", postulanteService.listarActivos().size());
        model.addAttribute("historial", postulanteService.listarHistorial().size());
        return "admin/dashboard";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("usuario", new UsuarioDTO());
        return "admin/usuarios";
    }

    @PostMapping("/admin/usuarios")
    public String guardarUsuario(@ModelAttribute UsuarioDTO usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/areas")
    public String areas(Model model) {
        model.addAttribute("areas", areaService.listar());
        model.addAttribute("area", new Area());
        return "admin/areas";
    }

    @PostMapping("/admin/areas")
    public String guardarArea(@ModelAttribute Area area) {
        areaService.guardar(area);
        return "redirect:/admin/areas";
    }

    @GetMapping("/admin/entrevistas")
    public String entrevistas(Model model) {
        model.addAttribute("entrevistas", entrevistaService.listar());
        model.addAttribute("postulantes", postulanteService.listarActivos());
        model.addAttribute("entrevista", new EntrevistaDTO());
        return "admin/entrevistas";
    }

    @PostMapping("/admin/entrevistas")
    public String guardarEntrevista(@ModelAttribute EntrevistaDTO entrevista) {
        entrevistaService.guardar(entrevista);
        return "redirect:/admin/entrevistas";
    }

    @GetMapping("/admin/preguntas")
    public String preguntas(Model model) {
        model.addAttribute("preguntas", preguntaService.listar());
        model.addAttribute("ofertas", ofertaService.listar());
        model.addAttribute("pregunta", new PreguntaDTO());
        return "admin/preguntas";
    }

    @PostMapping("/admin/preguntas")
    public String guardarPregunta(@ModelAttribute PreguntaDTO pregunta) {
        preguntaService.guardar(pregunta);
        return "redirect:/admin/preguntas";
    }
}
