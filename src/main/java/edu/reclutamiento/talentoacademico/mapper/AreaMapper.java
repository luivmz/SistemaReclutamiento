package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.AreaDTO;
import edu.reclutamiento.talentoacademico.model.Area;

public class AreaMapper {
    public static AreaDTO toDTO(Area area) {
        AreaDTO dto = new AreaDTO();
        dto.setId(area.getId());
        dto.setNombre(area.getNombre());
        dto.setDescripcion(area.getDescripcion());
        return dto;
    }
}
