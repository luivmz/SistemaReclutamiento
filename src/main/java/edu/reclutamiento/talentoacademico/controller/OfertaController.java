package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.service.AreaService;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OfertaController {
    private final OfertaService ofertaService;
    private final AreaService areaService;

    public OfertaController(OfertaService ofertaService, AreaService areaService) {
        this.ofertaService = ofertaService;
        this.areaService = areaService;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("ofertas", ofertaService.listarActivas());
        return "publico/inicio";
    }

    @GetMapping("/ofertas")
    public String ofertas(Model model) {
        model.addAttribute("ofertas", ofertaService.listarActivas());
        return "publico/ofertas";
    }

    @GetMapping("/areas")
    public String areas(Model model) {
        model.addAttribute("areas", areaService.listarActivas());
        return "publico/areas";
    }

    @GetMapping("/ofertas/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        OfertaDTO oferta = ofertaService.buscarActiva(id);
        if (oferta == null) {
            return "redirect:/ofertas";
        }
        model.addAttribute("oferta", oferta);
        return "publico/detalle-oferta";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "publico/contacto";
    }

    @PostMapping("/contacto")
    public String enviarContacto(Model model) {
        model.addAttribute("mensaje", "Solicitud recibida. Nos comunicaremos contigo pronto.");
        return "publico/contacto";
    }

    @GetMapping("/publicidad")
    public String publicidad() {
        return "publico/publicidad";
    }

    @GetMapping("/admin/ofertas")
    public String adminOfertas(Model model) {
        model.addAttribute("ofertas", ofertaService.listar());
        model.addAttribute("areas", areaService.listarActivas());
        model.addAttribute("oferta", new OfertaDTO());
        return "admin/ofertas-admin";
    }

    @GetMapping("/admin/ofertas/editar/{id}")
    public String editarOferta(@PathVariable Long id, Model model) {
        model.addAttribute("ofertas", ofertaService.listar());
        model.addAttribute("areas", areaService.listarActivas());
        model.addAttribute("oferta", ofertaService.buscar(id));
        return "admin/ofertas-admin";
    }

    @PostMapping("/admin/ofertas")
    public String guardarOferta(@ModelAttribute OfertaDTO oferta, Model model) {
        try {
            ofertaService.guardar(oferta);
            return "redirect:/admin/ofertas";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("ofertas", ofertaService.listar());
            model.addAttribute("areas", areaService.listarActivas());
            model.addAttribute("oferta", oferta);
            return "admin/ofertas-admin";
        }
    }

    @RequestMapping("/admin/ofertas/eliminar/{id}")
    public String eliminarOferta(@PathVariable Long id) {
        ofertaService.eliminar(id);
        return "redirect:/admin/ofertas";
    }

    @RequestMapping("/admin/ofertas/activar/{id}")
    public String activarOferta(@PathVariable Long id) {
        ofertaService.activar(id);
        return "redirect:/admin/ofertas";
    }
}
