package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.mapper.UsuarioMapper;
import edu.reclutamiento.talentoacademico.model.Usuario;
import edu.reclutamiento.talentoacademico.repository.UsuarioRepository;
import edu.reclutamiento.talentoacademico.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioMapper::toDTO).toList();
    }

    public UsuarioDTO guardar(UsuarioDTO dto) {
        return UsuarioMapper.toDTO(usuarioRepository.save(UsuarioMapper.toEntity(dto)));
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmailAndPasswordAndActivoTrue(email, password);
    }
}
