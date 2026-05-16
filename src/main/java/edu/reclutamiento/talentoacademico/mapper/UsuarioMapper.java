package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.model.RolUsuario;
import edu.reclutamiento.talentoacademico.model.Usuario;

public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setPassword(usuario.getPassword());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol() == null ? null : usuario.getRol().name());
        dto.setActivo(usuario.getActivo());
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol() == null ? RolUsuario.POSTULANTE : RolUsuario.valueOf(dto.getRol()));
        usuario.setActivo(dto.getActivo() == null || dto.getActivo());
        return usuario;
    }
}
