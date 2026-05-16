package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;

public class PostulanteMapper {
    public static PostulanteDTO toDTO(Postulante postulante) {
        PostulanteDTO dto = new PostulanteDTO();
        dto.setId(postulante.getId());
        dto.setNombre(postulante.getNombre());
        dto.setEmail(postulante.getEmail());
        dto.setTelefono(postulante.getTelefono());
        dto.setCv(postulante.getCv());
        dto.setEstado(postulante.getEstado().name());
        dto.setPuntaje(postulante.getPuntaje());
        dto.setAprobado(postulante.getAprobado());
        if (postulante.getOferta() != null) {
            dto.setOfertaId(postulante.getOferta().getId());
            dto.setOfertaTitulo(postulante.getOferta().getTitulo());
        }
        return dto;
    }

    public static Postulante toEntity(PostulanteDTO dto) {
        Postulante postulante = new Postulante();
        postulante.setId(dto.getId());
        postulante.setNombre(dto.getNombre());
        postulante.setEmail(dto.getEmail());
        postulante.setTelefono(dto.getTelefono());
        postulante.setCv(dto.getCv());
        postulante.setEstado(dto.getEstado() == null ? EstadoPostulante.ACTIVO : EstadoPostulante.valueOf(dto.getEstado()));
        postulante.setPuntaje(dto.getPuntaje() == null ? 0 : dto.getPuntaje());
        postulante.setAprobado(dto.getAprobado() != null && dto.getAprobado());
        return postulante;
    }
}
