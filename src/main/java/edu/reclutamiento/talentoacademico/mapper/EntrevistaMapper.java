package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.TipoEntrevista;

public class EntrevistaMapper {
    public static EntrevistaDTO toDTO(Entrevista entrevista) {
        EntrevistaDTO dto = new EntrevistaDTO();
        dto.setId(entrevista.getId());
        dto.setTipoEntrevista(entrevista.getTipoEntrevista().name());
        dto.setFecha(entrevista.getFecha());
        dto.setHora(entrevista.getHora());
        dto.setLugar(entrevista.getLugar());
        dto.setResultado(entrevista.getResultado());
        dto.setObservacion(entrevista.getObservacion());
        if (entrevista.getPostulante() != null) {
            dto.setPostulanteId(entrevista.getPostulante().getId());
            dto.setPostulanteNombre(entrevista.getPostulante().getNombre());
        }
        return dto;
    }

    public static Entrevista toEntity(EntrevistaDTO dto) {
        Entrevista entrevista = new Entrevista();
        entrevista.setId(dto.getId());
        entrevista.setTipoEntrevista(dto.getTipoEntrevista() == null ? TipoEntrevista.NORMAL : TipoEntrevista.valueOf(dto.getTipoEntrevista()));
        entrevista.setFecha(dto.getFecha());
        entrevista.setHora(dto.getHora());
        entrevista.setLugar(dto.getLugar());
        entrevista.setResultado(dto.getResultado());
        entrevista.setObservacion(dto.getObservacion());
        return entrevista;
    }
}
