package edu.reclutamiento.talentoacademico.controller.api;

import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.service.UsuarioService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    public UsuarioDTO guardar(@RequestBody UsuarioDTO usuario) {
        return usuarioService.guardar(usuario);
    }
}
