package edu.reclutamiento.talentoacademico.controller.api;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postulantes")
public class PostulanteRestController {
    private final PostulanteService postulanteService;

    public PostulanteRestController(PostulanteService postulanteService) {
        this.postulanteService = postulanteService;
    }

    @GetMapping
    public List<PostulanteDTO> listar() {
        return postulanteService.listar();
    }

    @GetMapping("/activos")
    public List<PostulanteDTO> activos() {
        return postulanteService.listarActivos();
    }

    @GetMapping("/historial")
    public List<PostulanteDTO> historial() {
        return postulanteService.listarHistorial();
    }

    @PostMapping
    public PostulanteDTO postular(@RequestBody PostulanteDTO postulante) {
        return postulanteService.postular(postulante);
    }

    @PutMapping("/{id}/aprobar")
    public void aprobar(@PathVariable Long id) {
        postulanteService.aprobar(id);
    }

    @PutMapping("/{id}/rechazar")
    public void rechazar(@PathVariable Long id) {
        postulanteService.rechazar(id);
    }
}
