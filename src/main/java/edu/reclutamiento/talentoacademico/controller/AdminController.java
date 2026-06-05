package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.service.AreaService;
import edu.reclutamiento.talentoacademico.service.EntrevistaService;
import edu.reclutamiento.talentoacademico.service.HistorialPostulanteService;
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
public class AdminController {
    private final UsuarioService usuarioService;
    private final AreaService areaService;
    private final OfertaService ofertaService;
    private final PostulanteService postulanteService;
    private final EntrevistaService entrevistaService;
    private final HistorialPostulanteService historialPostulanteService;

    public AdminController(UsuarioService usuarioService, AreaService areaService, OfertaService ofertaService,
                           PostulanteService postulanteService, EntrevistaService entrevistaService,
                           HistorialPostulanteService historialPostulanteService) {
        this.usuarioService = usuarioService;
        this.areaService = areaService;
        this.ofertaService = ofertaService;
        this.postulanteService = postulanteService;
        this.entrevistaService = entrevistaService;
        this.historialPostulanteService = historialPostulanteService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        cargarMetricas(model);
        return "admin/dashboard";
    }

    @GetMapping("/admin/metricas")
    public String metricas(Model model) {
        cargarMetricas(model);
        return "admin/dashboard";
    }

    private void cargarMetricas(Model model) {
        // Centraliza los contadores para que dashboard y /admin/metricas muestren los mismos datos.
        model.addAttribute("usuarios", usuarioService.listar().size());
        model.addAttribute("ofertas", ofertaService.listar().size());
        model.addAttribute("ofertasActivas", ofertaService.listarActivas().size());
        model.addAttribute("activos", postulanteService.listarActivos().size());
        model.addAttribute("historial", historialPostulanteService.listar().size());
        model.addAttribute("entrevistas", entrevistaService.listar().size());
        model.addAttribute("entrevistasProgramadas", entrevistaService.contarPorEstado(EstadoEntrevista.PROGRAMADA));
        model.addAttribute("aprobados", postulanteService.contarPorEstado(EstadoPostulante.APROBADO));
        model.addAttribute("rechazados", postulanteService.contarPorEstado(EstadoPostulante.RECHAZADO));
        model.addAttribute("cancelados", postulanteService.contarPorEstado(EstadoPostulante.CANCELADO));
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
    public String guardarUsuario(@ModelAttribute UsuarioDTO usuario, Model model) {
        try {
            usuarioService.guardar(usuario);
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("usuarios", usuarioService.listar());
            model.addAttribute("usuario", usuario);
            return "admin/usuarios";
        }
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
    public String guardarArea(@ModelAttribute Area area, Model model) {
        try {
            areaService.guardar(area);
            return "redirect:/admin/areas";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("areas", areaService.listar());
            model.addAttribute("area", area);
            return "admin/areas";
        }
    }

    @GetMapping("/admin/areas/eliminar/{id}")
    public String eliminarArea(@PathVariable Long id) {
        areaService.eliminar(id);
        return "redirect:/admin/areas";
    }

    @GetMapping("/admin/areas/activar/{id}")
    public String activarArea(@PathVariable Long id) {
        areaService.activar(id);
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
    public String guardarEntrevista(@ModelAttribute EntrevistaDTO entrevista, HttpSession session, Model model) {
        try {
            entrevistaService.guardar(entrevista, usuarioActual(session));
            return "redirect:/admin/entrevistas";
        } catch (IllegalStateException | IllegalArgumentException ex) {
            // La vista de entrevistas combina tabla y formulario; por eso se reconstruye todo el modelo al fallar.
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
        model.addAttribute("historial", historialPostulanteService.listar());
        return "admin/historial";
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
