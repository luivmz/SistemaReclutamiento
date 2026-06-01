package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.mapper.UsuarioMapper;
import edu.reclutamiento.talentoacademico.model.RolUsuario;
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

    public List<UsuarioDTO> listarPostulantes() {
        return usuarioRepository.findByRolAndActivoTrue(RolUsuario.POSTULANTE).stream().map(UsuarioMapper::toDTO).toList();
    }

    public UsuarioDTO buscar(Long id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toDTO).orElse(new UsuarioDTO());
    }

    public UsuarioDTO guardar(UsuarioDTO dto) {
        validar(dto);
        return UsuarioMapper.toDTO(usuarioRepository.save(UsuarioMapper.toEntity(dto)));
    }

    public void eliminar(Long id) {
        cambiarActivo(id, false);
    }

    public void cambiarActivo(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> login(String email, String password) {
        ValidationUtils.validarEmail(email);
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("El password es obligatorio.");
        }
        return usuarioRepository.findByEmailAndPasswordAndActivoTrue(email, password);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    private void validar(UsuarioDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        ValidationUtils.validarNombre(dto.getNombre(), "El nombre del usuario");
        ValidationUtils.validarEmail(dto.getEmail());
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("El password es obligatorio.");
        }
        ValidationUtils.validarTextoOpcional(dto.getTelefono(), "El telefono", 40);
    }
}
