package edu.reclutamiento.talentoacademico.controller.api;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
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
        OfertaDTO dto = ofertaService.buscar(id);
        if (dto == null) {
            throw new NoSuchElementException("Oferta no encontrada con id " + id);
        }
        return dto;
    }

    @PostMapping
    public OfertaDTO guardar(@RequestBody OfertaDTO oferta) {
        return ofertaService.guardar(oferta);
    }

    @PutMapping("/{id}")
    public OfertaDTO actualizar(@PathVariable Long id, @RequestBody OfertaDTO oferta) {
        return ofertaService.actualizar(id, oferta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ofertaService.eliminarReal(id);
        return ResponseEntity.noContent().build();
    }
}
