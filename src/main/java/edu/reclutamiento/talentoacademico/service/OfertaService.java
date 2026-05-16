package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import java.util.List;

public interface OfertaService {
    List<OfertaDTO> listar();
    List<OfertaDTO> listarActivas();
    OfertaDTO buscar(Long id);
    OfertaDTO guardar(OfertaDTO dto);
    OfertaDTO actualizar(Long id, OfertaDTO dto);
    void activar(Long id);
    void eliminar(Long id);
    void eliminarReal(Long id);
}
