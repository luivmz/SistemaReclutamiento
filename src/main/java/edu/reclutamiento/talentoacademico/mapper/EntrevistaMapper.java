package edu.reclutamiento.talentoacademico.mapper;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import edu.reclutamiento.talentoacademico.model.ResultadoEntrevista;
import edu.reclutamiento.talentoacademico.model.TipoEntrevista;

public class EntrevistaMapper {
    public static EntrevistaDTO toDTO(Entrevista entrevista) {
        EntrevistaDTO dto = new EntrevistaDTO();
        dto.setId(entrevista.getId());
        dto.setTipoEntrevista(entrevista.getTipoEntrevista() == null ? null : entrevista.getTipoEntrevista().name());
        dto.setFecha(entrevista.getFecha());
        dto.setHora(entrevista.getHora());
        dto.setLugar(entrevista.getLugar());
        dto.setModalidad(entrevista.getModalidad());
        dto.setResultado(entrevista.getResultado() == null ? null : entrevista.getResultado().name());
        dto.setObservacion(entrevista.getObservacion());
        dto.setEstadoEntrevista(entrevista.getEstadoEntrevista() == null ? null : entrevista.getEstadoEntrevista().name());
        if (entrevista.getPostulante() != null) {
            dto.setPostulanteId(entrevista.getPostulante().getId());
            dto.setPostulanteNombre(entrevista.getPostulante().getNombre());
        }
        return dto;
    }

    public static Entrevista toEntity(EntrevistaDTO dto) {
        Entrevista entrevista = new Entrevista();
        entrevista.setId(dto.getId());
        entrevista.setTipoEntrevista(dto.getTipoEntrevista() == null || dto.getTipoEntrevista().isBlank()
                ? TipoEntrevista.NORMAL
                : TipoEntrevista.valueOf(dto.getTipoEntrevista()));
        entrevista.setFecha(dto.getFecha());
        entrevista.setHora(dto.getHora());
        entrevista.setLugar(dto.getLugar());
        entrevista.setModalidad(dto.getModalidad());
        entrevista.setResultado(dto.getResultado() == null || dto.getResultado().isBlank()
                ? ResultadoEntrevista.PENDIENTE
                : ResultadoEntrevista.valueOf(dto.getResultado()));
        entrevista.setObservacion(dto.getObservacion());
        entrevista.setEstadoEntrevista(dto.getEstadoEntrevista() == null || dto.getEstadoEntrevista().isBlank()
                ? EstadoEntrevista.PROGRAMADA
                : EstadoEntrevista.valueOf(dto.getEstadoEntrevista()));
        return entrevista;
    }
}
