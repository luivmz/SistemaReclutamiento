package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import java.util.List;

public interface PostulanteService {
    List<PostulanteDTO> listar();
    List<PostulanteDTO> listarActivos();
    List<PostulanteDTO> listarHistorial();
    List<PostulanteDTO> listarPorEmail(String email);
    List<PostulanteDTO> listarPorUsuario(Long usuarioId);
    PostulanteDTO buscar(Long id);
    PostulanteDTO postular(PostulanteDTO dto, Long usuarioId);
    PostulanteDTO actualizar(PostulanteDTO dto, String registradoPor);
    boolean yaPostulo(Long usuarioId, Long ofertaId);
    long contarPorEstado(EstadoPostulante estado);
    long contarPorUsuario(Long usuarioId);
    long contarPorUsuarioYEstado(Long usuarioId, String estado);
    void cambiarEstado(Long id, String estado, String registradoPor);
    void cambiarEstado(Long id, EstadoPostulante estado, String registradoPor);
    void aprobar(Long id, String registradoPor);
    void rechazar(Long id, String registradoPor);
    void marcarEnEntrevista(Long id, String registradoPor);
}
