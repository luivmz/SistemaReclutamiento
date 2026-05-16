package edu.reclutamiento.talentoacademico.controller.api;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public PostulanteDTO buscar(@PathVariable Long id) {
        PostulanteDTO dto = postulanteService.buscar(id);
        if (dto == null) {
            throw new NoSuchElementException("Postulante no encontrado con id " + id);
        }
        return dto;
    }

    @PostMapping
    public PostulanteDTO postular(@RequestBody PostulanteDTO postulante) {
        // Si el cuerpo trae usuarioId, se mantiene el flujo original de postulacion ligada a un usuario.
        // En caso contrario, se delega a un alta simple (REST/TDD) que solo exige nombre, email y oferta.
        if (postulante.getUsuarioId() != null) {
            return postulanteService.postular(postulante, postulante.getUsuarioId());
        }
        return postulanteService.crear(postulante);
    }

    @PutMapping("/{id}")
    public PostulanteDTO actualizar(@PathVariable Long id, @RequestBody PostulanteDTO postulante) {
        return postulanteService.actualizarPostulante(id, postulante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        postulanteService.eliminarReal(id);
        return ResponseEntity.noContent().build();
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
