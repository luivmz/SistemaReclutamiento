package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.service.AreaService;
import edu.reclutamiento.talentoacademico.service.EntrevistaService;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import edu.reclutamiento.talentoacademico.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    private final UsuarioService usuarioService;
    private final AreaService areaService;
    private final OfertaService ofertaService;
    private final PostulanteService postulanteService;
    private final EntrevistaService entrevistaService;

    public AdminController(UsuarioService usuarioService, AreaService areaService, OfertaService ofertaService,
                           PostulanteService postulanteService, EntrevistaService entrevistaService) {
        this.usuarioService = usuarioService;
        this.areaService = areaService;
        this.ofertaService = ofertaService;
        this.postulanteService = postulanteService;
        this.entrevistaService = entrevistaService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("usuarios", usuarioService.listar().size());
        model.addAttribute("ofertas", ofertaService.listar().size());
        model.addAttribute("activos", postulanteService.listarActivos().size());
        model.addAttribute("historial", postulanteService.listarHistorial().size());
        model.addAttribute("entrevistas", entrevistaService.listar().size());
        return "admin/dashboard";
    }

    @GetMapping("/admin/metricas")
    public String metricas(Model model) {
        model.addAttribute("usuarios", usuarioService.listar().size());
        model.addAttribute("ofertas", ofertaService.listar().size());
        model.addAttribute("activos", postulanteService.listarActivos().size());
        model.addAttribute("historial", postulanteService.listarHistorial().size());
        model.addAttribute("entrevistas", entrevistaService.listar().size());
        return "admin/dashboard";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("usuario", new UsuarioDTO());
        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("usuario", usuarioService.buscar(id));
        return "admin/usuarios";
    }

    @PostMapping("/admin/usuarios")
    public String guardarUsuario(@ModelAttribute UsuarioDTO usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id) {
        usuarioService.cambiarActivo(id, false);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/activar/{id}")
    public String activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarActivo(id, true);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/areas")
    public String areas(Model model) {
        model.addAttribute("areas", areaService.listar());
        model.addAttribute("area", new Area());
        return "admin/areas";
    }

    @GetMapping("/admin/areas/editar/{id}")
    public String editarArea(@PathVariable Long id, Model model) {
        model.addAttribute("areas", areaService.listar());
        model.addAttribute("area", areaService.buscar(id));
        return "admin/areas";
    }

    @PostMapping("/admin/areas")
    public String guardarArea(@ModelAttribute Area area) {
        areaService.guardar(area);
        return "redirect:/admin/areas";
    }

    @GetMapping("/admin/areas/eliminar/{id}")
    public String eliminarArea(@PathVariable Long id) {
        areaService.eliminar(id);
        return "redirect:/admin/areas";
    }

    @GetMapping("/admin/entrevistas")
    public String entrevistas(Model model) {
        model.addAttribute("entrevistas", entrevistaService.listar());
        model.addAttribute("postulantes", postulanteService.listarActivos());
        model.addAttribute("entrevista", new EntrevistaDTO());
        return "admin/entrevistas";
    }

    @GetMapping("/admin/entrevistas/editar/{id}")
    public String editarEntrevista(@PathVariable Long id, Model model) {
        model.addAttribute("entrevistas", entrevistaService.listar());
        model.addAttribute("postulantes", postulanteService.listarActivos());
        model.addAttribute("entrevista", entrevistaService.buscar(id));
        return "admin/entrevistas";
    }

    @PostMapping("/admin/entrevistas")
    public String guardarEntrevista(@ModelAttribute EntrevistaDTO entrevista, Model model) {
        try {
            entrevistaService.guardar(entrevista);
            return "redirect:/admin/entrevistas";
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("entrevistas", entrevistaService.listar());
            model.addAttribute("postulantes", postulanteService.listarActivos());
            model.addAttribute("entrevista", entrevista);
            return "admin/entrevistas";
        }
    }

    @GetMapping("/admin/entrevistas/eliminar/{id}")
    public String eliminarEntrevista(@PathVariable Long id) {
        entrevistaService.eliminar(id);
        return "redirect:/admin/entrevistas";
    }

    @GetMapping("/admin/historial")
    public String historial(Model model) {
        model.addAttribute("historial", postulanteService.listarHistorial());
        return "admin/historial";
    }
}
