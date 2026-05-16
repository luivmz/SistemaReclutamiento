package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import java.util.List;

public interface PostulanteService {
    List<PostulanteDTO> listar();
    List<PostulanteDTO> listarActivos();
    List<PostulanteDTO> listarHistorial();
    List<PostulanteDTO> listarPorEmail(String email);
    PostulanteDTO postular(PostulanteDTO dto);
    PostulanteDTO actualizar(PostulanteDTO dto);
    void aprobar(Long id);
    void rechazar(Long id);
    void finalizar(Long id);
}
