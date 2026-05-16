package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.PreguntaDTO;
import java.util.List;

public interface PreguntaService {
    List<PreguntaDTO> listar();
    PreguntaDTO guardar(PreguntaDTO dto);
}
