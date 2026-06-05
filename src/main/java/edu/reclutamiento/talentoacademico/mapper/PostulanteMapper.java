package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;

public class PostulanteMapper {
    public static PostulanteDTO toDTO(Postulante postulante) {
        // Convierte la entidad Postulante en un DTO con los datos necesarios para la vista.
        // Asi evitamos exponer directamente la entidad JPA y todas sus relaciones.
        PostulanteDTO dto = new PostulanteDTO();
        dto.setId(postulante.getId());
        dto.setNombre(postulante.getNombre());
        dto.setEmail(postulante.getEmail());
        dto.setTelefono(postulante.getTelefono());
        dto.setExperiencia(postulante.getExperiencia());
        dto.setHabilidades(postulante.getHabilidades());
        dto.setCv(postulante.getCv());
        dto.setFechaPostulacion(postulante.getFechaPostulacion() == null ? "" : postulante.getFechaPostulacion().toString());
        dto.setEstado(postulante.getEstado().name());
        dto.setAprobado(postulante.getAprobado());
        dto.setObservacion(postulante.getObservacion());
        if (postulante.getOferta() != null) {
            // De la relacion con la oferta copiamos datos simples para mostrarlos directamente en el JSP.
            dto.setOfertaId(postulante.getOferta().getId());
            dto.setOfertaTitulo(postulante.getOferta().getTitulo());
            if (postulante.getOferta().getArea() != null) {
                // El nombre del area es un dato derivado de la oferta asociada.
                dto.setAreaNombre(postulante.getOferta().getArea().getNombre());
            }
        }
        if (postulante.getUsuario() != null) {
            // Solo enviamos el id del usuario relacionado, no la entidad Usuario completa.
            dto.setUsuarioId(postulante.getUsuario().getId());
        }
        return dto;
    }

    public static Postulante toEntity(PostulanteDTO dto) {
        Postulante postulante = new Postulante();
        postulante.setId(dto.getId());
        postulante.setNombre(dto.getNombre());
        postulante.setEmail(dto.getEmail());
        postulante.setTelefono(dto.getTelefono());
        postulante.setExperiencia(dto.getExperiencia());
        postulante.setHabilidades(dto.getHabilidades());
        postulante.setCv(dto.getCv());
        postulante.setEstado(dto.getEstado() == null ? EstadoPostulante.POSTULADO : EstadoPostulante.valueOf(dto.getEstado()));
        postulante.setAprobado(dto.getAprobado() != null && dto.getAprobado());
        postulante.setObservacion(dto.getObservacion());
        return postulante;
    }
}
