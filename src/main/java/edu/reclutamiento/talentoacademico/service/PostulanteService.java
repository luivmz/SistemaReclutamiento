package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
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
    long contarPorUsuario(Long usuarioId);
    long contarPorUsuarioYEstado(Long usuarioId, String estado);
    void marcarEnEvaluacion(Long id);
    void registrarEvaluacion(Long id, int puntaje, boolean aprobado);
    void cambiarEstado(Long id, String estado);
    void aprobar(Long id);
    void rechazar(Long id);
    void finalizar(Long id);
}
