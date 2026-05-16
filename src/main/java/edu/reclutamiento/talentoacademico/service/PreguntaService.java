package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.PreguntaDTO;
import java.util.List;

public interface PreguntaService {
    List<PreguntaDTO> listar();
    PreguntaDTO buscar(Long id);
    PreguntaDTO guardar(PreguntaDTO dto);
    void eliminar(Long id);
}
