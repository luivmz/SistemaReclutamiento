package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.mapper.PostulanteMapper;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.model.Usuario;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.repository.UsuarioRepository;
import edu.reclutamiento.talentoacademico.service.HistorialPostulanteService;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostulanteServiceImpl implements PostulanteService {
    private final PostulanteRepository postulanteRepository;
    private final OfertaRepository ofertaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialPostulanteService historialPostulanteService;

    public PostulanteServiceImpl(PostulanteRepository postulanteRepository, OfertaRepository ofertaRepository,
                                 UsuarioRepository usuarioRepository,
                                 HistorialPostulanteService historialPostulanteService) {
        this.postulanteRepository = postulanteRepository;
        this.ofertaRepository = ofertaRepository;
        this.usuarioRepository = usuarioRepository;
        this.historialPostulanteService = historialPostulanteService;
    }

    public List<PostulanteDTO> listar() {
        return postulanteRepository.findAll().stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarActivos() {
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.POSTULADO,
                EstadoPostulante.EN_ENTREVISTA
        )).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarHistorial() {
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.APROBADO,
                EstadoPostulante.RECHAZADO
        )).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarPorEmail(String email) {
        return postulanteRepository.findByEmail(email).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarPorUsuario(Long usuarioId) {
        return postulanteRepository.findByUsuarioId(usuarioId).stream().map(PostulanteMapper::toDTO).toList();
    }

    public PostulanteDTO buscar(Long id) {
        return postulanteRepository.findById(id).map(PostulanteMapper::toDTO).orElse(null);
    }

    public PostulanteDTO postular(PostulanteDTO dto, Long usuarioId) {
        if (dto.getOfertaId() != null && yaPostulo(usuarioId, dto.getOfertaId())) {
            throw new IllegalStateException("Ya postulaste a esta oferta.");
        }
        dto.setId(null);
        Postulante postulante = PostulanteMapper.toEntity(dto);
        postulante.setEstado(EstadoPostulante.POSTULADO);
        postulante.setAprobado(false);
        asignarOferta(dto, postulante);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        postulante.setUsuario(usuario);
        postulante.setNombre(usuario.getNombre());
        postulante.setEmail(usuario.getEmail());
        if (postulante.getTelefono() == null || postulante.getTelefono().isBlank()) {
            postulante.setTelefono(usuario.getTelefono());
        }
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public PostulanteDTO actualizar(PostulanteDTO dto, String registradoPor) {
        Postulante postulante = postulanteRepository.findById(dto.getId()).orElseThrow();
        EstadoPostulante estadoAnterior = postulante.getEstado();
        postulante.setNombre(dto.getNombre());
        postulante.setEmail(dto.getEmail());
        postulante.setTelefono(dto.getTelefono());
        postulante.setExperiencia(dto.getExperiencia());
        postulante.setHabilidades(dto.getHabilidades());
        postulante.setCv(dto.getCv());
        postulante.setObservacion(dto.getObservacion());
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            postulante.setEstado(EstadoPostulante.valueOf(dto.getEstado()));
        }
        asignarOferta(dto, postulante);
        Postulante guardado = postulanteRepository.save(postulante);
        historialPostulanteService.registrarCambioEstado(
                guardado, estadoAnterior, guardado.getEstado(), dto.getObservacion(), registradoPor);
        return PostulanteMapper.toDTO(guardado);
    }

    public void aprobar(Long id, String registradoPor) {
        cambiarEstadoInterno(id, EstadoPostulante.APROBADO, true, "Postulante aprobado.", registradoPor);
    }

    public void rechazar(Long id, String registradoPor) {
        cambiarEstadoInterno(id, EstadoPostulante.RECHAZADO, false, "Postulante rechazado.", registradoPor);
    }

    public void marcarEnEntrevista(Long id, String registradoPor) {
        cambiarEstadoInterno(id, EstadoPostulante.EN_ENTREVISTA, false, null, registradoPor);
    }

    public boolean yaPostulo(Long usuarioId, Long ofertaId) {
        return postulanteRepository.existsByUsuarioIdAndOfertaId(usuarioId, ofertaId);
    }

    public long contarPorEstado(EstadoPostulante estado) {
        return postulanteRepository.countByEstado(estado);
    }

    public long contarPorUsuario(Long usuarioId) {
        return postulanteRepository.countByUsuarioId(usuarioId);
    }

    public long contarPorUsuarioYEstado(Long usuarioId, String estado) {
        return postulanteRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPostulante.valueOf(estado));
    }

    public void cambiarEstado(Long id, String estado, String registradoPor) {
        cambiarEstado(id, EstadoPostulante.valueOf(estado), registradoPor);
    }

    public void cambiarEstado(Long id, EstadoPostulante estado, String registradoPor) {
        cambiarEstadoInterno(id, estado, estado == EstadoPostulante.APROBADO, null, registradoPor);
    }

    private void cambiarEstadoInterno(Long id, EstadoPostulante estado, boolean aprobado, String observacion,
                                      String registradoPor) {
        Postulante postulante = postulanteRepository.findById(id).orElseThrow();
        EstadoPostulante estadoAnterior = postulante.getEstado();
        postulante.setEstado(estado);
        postulante.setAprobado(aprobado);
        if (observacion != null) {
            postulante.setObservacion(observacion);
        }
        Postulante guardado = postulanteRepository.save(postulante);
        historialPostulanteService.registrarCambioEstado(
                guardado, estadoAnterior, estado, observacion, registradoPor);
    }

    private void asignarOferta(PostulanteDTO dto, Postulante postulante) {
        if (dto.getOfertaId() != null) {
            OfertaLaboral oferta = ofertaRepository.findById(dto.getOfertaId()).orElse(null);
            postulante.setOferta(oferta);
        }
    }
}
