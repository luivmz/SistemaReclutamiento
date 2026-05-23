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
    PostulanteDTO actualizar(PostulanteDTO dto);
    boolean yaPostulo(Long usuarioId, Long ofertaId);
    long contarPorEstado(EstadoPostulante estado);
    long contarPorUsuario(Long usuarioId);
    long contarPorUsuarioYEstado(Long usuarioId, String estado);
    void cambiarEstado(Long id, String estado);
    void cambiarEstado(Long id, EstadoPostulante estado);
    void aprobar(Long id);
    void rechazar(Long id);
    void marcarEnEntrevista(Long id);
}
