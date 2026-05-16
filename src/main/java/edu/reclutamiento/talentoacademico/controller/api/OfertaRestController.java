package edu.reclutamiento.talentoacademico.controller.api;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaRestController {
    private final OfertaService ofertaService;

    public OfertaRestController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @GetMapping
    public List<OfertaDTO> listar() {
        return ofertaService.listar();
    }

    @GetMapping("/{id}")
    public OfertaDTO buscar(@PathVariable Long id) {
        return ofertaService.buscar(id);
    }

    @PostMapping
    public OfertaDTO guardar(@RequestBody OfertaDTO oferta) {
        return ofertaService.guardar(oferta);
    }
}
