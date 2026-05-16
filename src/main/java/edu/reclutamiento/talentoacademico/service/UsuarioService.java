package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioDTO> listar();
    UsuarioDTO guardar(UsuarioDTO dto);
    void eliminar(Long id);
    Optional<Usuario> login(String email, String password);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
}
