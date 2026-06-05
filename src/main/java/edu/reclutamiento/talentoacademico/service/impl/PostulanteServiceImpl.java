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
        // map usa la referencia PostulanteMapper::toDTO para convertir cada entidad en un DTO.
        return postulanteRepository.findAll().stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarActivos() {
        // Solo POSTULADO y EN_ENTREVISTA son procesos activos; los cerrados van al historial.
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.POSTULADO,
                EstadoPostulante.EN_ENTREVISTA
        )).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarHistorial() {
        // Estos enums representan estados finales y se muestran en la seccion de historial.
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.APROBADO,
                EstadoPostulante.RECHAZADO,
                EstadoPostulante.CANCELADO
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
        validarPostulacion(dto);
        // Evita duplicar postulaciones del mismo usuario a la misma oferta antes de crear la entidad.
        if (dto.getOfertaId() != null && yaPostulo(usuarioId, dto.getOfertaId())) {
            throw new IllegalStateException("Ya postulaste a esta oferta.");
        }

        // La postulacion siempre nace como registro nuevo y en estado POSTULADO.
        dto.setId(null);
        Postulante postulante = PostulanteMapper.toEntity(dto);
        postulante.setEstado(EstadoPostulante.POSTULADO);
        postulante.setAprobado(false);
        asignarOferta(dto, postulante);

        // Los datos principales vienen del usuario autenticado para evitar que se postule con otra identidad.
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        postulante.setUsuario(usuario);
        postulante.setNombre(usuario.getNombre());
        postulante.setEmail(usuario.getEmail());
        if (postulante.getTelefono() == null || postulante.getTelefono().isBlank()) {
            postulante.setTelefono(usuario.getTelefono());
        }
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    // Actualiza el postulante y registra su cambio de estado como una sola operacion.
    // Si falla el historial, Spring revierte tambien la actualizacion.
    @Transactional
    public PostulanteDTO actualizar(PostulanteDTO dto, String registradoPor) {
        validarPostulacion(dto);
        Postulante postulante = postulanteRepository.findById(dto.getId()).orElseThrow();
        EstadoPostulante estadoAnterior = postulante.getEstado();

        // Se actualizan los datos editables desde el formulario de administracion.
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
        // Si el estado no cambio, el servicio de historial no registra nada.
        historialPostulanteService.registrarCambioEstado(
                guardado, estadoAnterior, guardado.getEstado(), dto.getObservacion(), registradoPor);
        return PostulanteMapper.toDTO(guardado);
    }

    // El cambio de estado y su historial deben confirmarse o revertirse juntos.
    @Transactional
    public void aprobar(Long id, String registradoPor) {
        cambiarEstadoInterno(id, EstadoPostulante.APROBADO, true, "Postulante aprobado.", registradoPor);
    }

    // Rechaza al postulante y registra el mismo cambio en historial.
    @Transactional
    public void rechazar(Long id, String registradoPor) {
        cambiarEstadoInterno(id, EstadoPostulante.RECHAZADO, false, "Postulante rechazado.", registradoPor);
    }

    // Actualiza el estado a EN_ENTREVISTA y conserva la trazabilidad del movimiento.
    @Transactional
    public void marcarEnEntrevista(Long id, String registradoPor) {
        cambiarEstadoInterno(id, EstadoPostulante.EN_ENTREVISTA, false, null, registradoPor);
    }

    // Cancela la postulacion y registra el movimiento en historial dentro de la misma transaccion.
    @Transactional
    public void cancelar(Long id, Long usuarioId) {
        Postulante postulante = postulanteRepository.findById(id).orElseThrow();
        // Un postulante solo puede cancelar registros asociados a su propia cuenta.
        if (postulante.getUsuario() == null || !postulante.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalStateException("No puedes cancelar una postulacion que no te pertenece.");
        }
        // Los estados finales ya forman parte del historial y no deben volver a cambiarse desde la vista publica.
        if (postulante.getEstado() != EstadoPostulante.POSTULADO
                && postulante.getEstado() != EstadoPostulante.EN_ENTREVISTA) {
            throw new IllegalStateException("Solo se pueden cancelar postulaciones activas.");
        }

        EstadoPostulante estadoAnterior = postulante.getEstado();
        postulante.setEstado(EstadoPostulante.CANCELADO);
        postulante.setAprobado(false);
        postulante.setObservacion("Postulacion cancelada por el postulante");
        Postulante guardado = postulanteRepository.save(postulante);
        // El historial deja evidencia academica del cambio de estado realizado por el postulante.
        historialPostulanteService.registrarCambioEstado(
                guardado, estadoAnterior, EstadoPostulante.CANCELADO,
                "Postulacion cancelada por el postulante", guardado.getNombre());
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
        // valueOf convierte el texto recibido en el enum usado por la consulta del repositorio.
        return postulanteRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPostulante.valueOf(estado));
    }

    // Esta sobrecarga recibe el estado como texto y mantiene unido el cambio con su historial.
    @Transactional
    public void cambiarEstado(Long id, String estado, String registradoPor) {
        cambiarEstado(id, EstadoPostulante.valueOf(estado), registradoPor);
    }

    // Esta sobrecarga permite recibir directamente el enum sin perder la transaccion.
    @Transactional
    public void cambiarEstado(Long id, EstadoPostulante estado, String registradoPor) {
        cambiarEstadoInterno(id, estado, estado == EstadoPostulante.APROBADO, null, registradoPor);
    }

    private void cambiarEstadoInterno(Long id, EstadoPostulante estado, boolean aprobado, String observacion,
                                      String registradoPor) {
        Postulante postulante = postulanteRepository.findById(id).orElseThrow();
        EstadoPostulante estadoAnterior = postulante.getEstado();

        // Centraliza las transiciones hechas por acciones rapidas: aprobar, rechazar o mover de estado.
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

    private void validarPostulacion(PostulanteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La postulacion no puede ser nula.");
        }
        ValidationUtils.validarNombre(dto.getNombre(), "El nombre del postulante");
        ValidationUtils.validarEmail(dto.getEmail());
        ValidationUtils.validarTextoOpcional(dto.getTelefono(), "El telefono", 40);
        ValidationUtils.validarTextoOpcional(dto.getExperiencia(), "La experiencia", 700);
        ValidationUtils.validarTextoOpcional(dto.getHabilidades(), "Las habilidades", 700);
        ValidationUtils.validarTextoOpcional(dto.getCv(), "El enlace del CV", 255);
        ValidationUtils.validarTextoOpcional(dto.getObservacion(), "La observacion", 700);
        if (dto.getOfertaId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una oferta.");
        }
    }
}
