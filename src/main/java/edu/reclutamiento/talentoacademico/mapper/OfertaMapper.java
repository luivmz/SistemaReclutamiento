package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;

public class OfertaMapper {
    public static OfertaDTO toDTO(OfertaLaboral oferta) {
        OfertaDTO dto = new OfertaDTO();
        dto.setId(oferta.getId());
        dto.setTitulo(oferta.getTitulo());
        dto.setDescripcion(oferta.getDescripcion());
        dto.setVacantes(oferta.getVacantes());
        dto.setActiva(oferta.getActiva());
        if (oferta.getArea() != null) {
            dto.setAreaId(oferta.getArea().getId());
            dto.setAreaNombre(oferta.getArea().getNombre());
        }
        return dto;
    }
}
