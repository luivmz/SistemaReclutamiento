package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import java.util.List;

public interface EntrevistaService {
    List<EntrevistaDTO> listar();
    List<EntrevistaDTO> listarPorPostulante(Long postulanteId);
    EntrevistaDTO guardar(EntrevistaDTO dto);
}
