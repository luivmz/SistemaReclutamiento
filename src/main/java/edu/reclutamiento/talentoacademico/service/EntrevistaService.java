package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import java.util.List;

public interface EntrevistaService {
    List<EntrevistaDTO> listar();
    List<EntrevistaDTO> listarPorPostulante(Long postulanteId);
    EntrevistaDTO buscar(Long id);
    EntrevistaDTO guardar(EntrevistaDTO dto, String registradoPor);
    long contarPorEstado(EstadoEntrevista estado);
    void eliminar(Long id);
}
