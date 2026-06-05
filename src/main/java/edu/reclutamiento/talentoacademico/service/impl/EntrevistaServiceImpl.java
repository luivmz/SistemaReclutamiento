package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.mapper.EntrevistaMapper;
import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.repository.EntrevistaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.service.EntrevistaService;
import edu.reclutamiento.talentoacademico.service.HistorialPostulanteService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntrevistaServiceImpl implements EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;
    private final PostulanteRepository postulanteRepository;
    private final HistorialPostulanteService historialPostulanteService;

    public EntrevistaServiceImpl(EntrevistaRepository entrevistaRepository, PostulanteRepository postulanteRepository,
                                 HistorialPostulanteService historialPostulanteService) {
        this.entrevistaRepository = entrevistaRepository;
        this.postulanteRepository = postulanteRepository;
        this.historialPostulanteService = historialPostulanteService;
    }

    public List<EntrevistaDTO> listar() {
        // Convertimos cada entidad del listado en un DTO mediante una referencia funcional.
        return entrevistaRepository.findAll().stream().map(EntrevistaMapper::toDTO).toList();
    }

    public List<EntrevistaDTO> listarPorPostulante(Long postulanteId) {
        return entrevistaRepository.findByPostulanteId(postulanteId).stream().map(EntrevistaMapper::toDTO).toList();
    }

    public EntrevistaDTO buscar(Long id) {
        return entrevistaRepository.findById(id).map(EntrevistaMapper::toDTO).orElse(new EntrevistaDTO());
    }

    // Guarda la entrevista, actualiza al postulante y registra el historial como una sola operacion.
    // Si una parte falla, Spring revierte todos los cambios relacionados.
    @Transactional
    public EntrevistaDTO guardar(EntrevistaDTO dto, String registradoPor) {
        validar(dto);
        boolean esNueva = dto.getId() == null;
        // La restriccion de fecha pasada aplica solo al crear; una edicion puede mantener una entrevista antigua.
        if (esNueva && dto.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede programar una entrevista con fecha pasada.");
        }

        Entrevista datosFormulario = EntrevistaMapper.toEntity(dto);
        Entrevista entrevista;
        Postulante postulante;

        if (esNueva) {
            postulante = postulanteRepository.findById(dto.getPostulanteId())
                    .orElseThrow(() -> new IllegalArgumentException("Postulante no encontrado."));
            validarPostulanteActivo(postulante);
            entrevista = datosFormulario;
            entrevista.setPostulante(postulante);
            // Toda entrevista nueva inicia PROGRAMADA; REALIZADA y CANCELADA son estados posteriores.
            entrevista.setEstadoEntrevista(EstadoEntrevista.PROGRAMADA);
        } else {
            entrevista = entrevistaRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Entrevista no encontrada."));
            postulante = entrevista.getPostulante();
            validarPostulanteActivo(postulante);

            // Al editar se conserva el postulante original para evitar reasignaciones accidentales.
            entrevista.setTipoEntrevista(datosFormulario.getTipoEntrevista());
            entrevista.setFecha(datosFormulario.getFecha());
            entrevista.setHora(datosFormulario.getHora());
            entrevista.setLugar(datosFormulario.getLugar());
            entrevista.setModalidad(datosFormulario.getModalidad());
            entrevista.setObservacion(datosFormulario.getObservacion());

            if (entrevista.getResultadoEntrevista() != null
                    && datosFormulario.getEstadoEntrevista() != EstadoEntrevista.REALIZADA) {
                throw new IllegalStateException("Una entrevista con resultado debe permanecer REALIZADA.");
            }
            entrevista.setEstadoEntrevista(datosFormulario.getEstadoEntrevista());
        }

        Entrevista guardada = entrevistaRepository.save(entrevista);

        // Solo la primera programacion mueve al postulante a EN_ENTREVISTA y registra historial.
        if (postulante != null && esNueva) {
            marcarPostulanteEnEntrevista(postulante, registradoPor);
        }

        return EntrevistaMapper.toDTO(guardada);
    }

    // El postulante puede cancelar una entrevista programada,
    // pero esto no implica cancelar toda la postulacion.
    // @Transactional une la actualizacion de la entrevista con el registro del historial.
    @Transactional
    public Long cancelarEntrevista(Long entrevistaId, Long usuarioId) {
        Entrevista entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new IllegalArgumentException("Entrevista no encontrada."));
        Postulante postulante = entrevista.getPostulante();

        if (postulante == null || postulante.getUsuario() == null
                || !postulante.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No puedes cancelar una entrevista que no te pertenece.");
        }
        if (postulante.getEstado() == EstadoPostulante.CANCELADO) {
            throw new IllegalStateException("La postulacion ya se encuentra cancelada.");
        }
        if (entrevista.getEstadoEntrevista() != EstadoEntrevista.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden cancelar entrevistas programadas.");
        }
        if (entrevista.getResultadoEntrevista() != null) {
            throw new IllegalStateException("No se puede cancelar una entrevista que ya tiene resultado.");
        }

        entrevista.setEstadoEntrevista(EstadoEntrevista.CANCELADA);
        entrevista.setObservacion("Entrevista cancelada por el postulante.");
        entrevistaRepository.save(entrevista);

        String tipo = entrevista.getTipoEntrevista() == null
                ? "Entrevista"
                : "Entrevista " + entrevista.getTipoEntrevista().name().toLowerCase();
        // Se registra historial para mantener trazabilidad sin cambiar el estado del postulante.
        historialPostulanteService.registrarEvento(
                postulante, tipo + " cancelada por el postulante.", postulante.getNombre());

        return postulante.getId();
    }

    public boolean tieneEntrevistasProgramadas(Long postulanteId) {
        return entrevistaRepository.existsByPostulanteIdAndEstadoEntrevista(
                postulanteId, EstadoEntrevista.PROGRAMADA);
    }

    public void eliminar(Long id) {
        entrevistaRepository.deleteById(id);
    }

    public long contarPorEstado(EstadoEntrevista estado) {
        return entrevistaRepository.countByEstadoEntrevista(estado);
    }

    private void validarPostulanteActivo(Postulante postulante) {
        EstadoPostulante estado = postulante.getEstado();
        if (estado == EstadoPostulante.APROBADO
                || estado == EstadoPostulante.RECHAZADO
                || estado == EstadoPostulante.CANCELADO) {
            throw new IllegalStateException("Solo se puede programar entrevista a un postulante activo.");
        }
    }

    private void marcarPostulanteEnEntrevista(Postulante postulante, String registradoPor) {
        if (postulante.getEstado() != EstadoPostulante.APROBADO
                && postulante.getEstado() != EstadoPostulante.RECHAZADO
                && postulante.getEstado() != EstadoPostulante.CANCELADO) {
            EstadoPostulante estadoAnterior = postulante.getEstado();
            postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
            Postulante guardado = postulanteRepository.save(postulante);
            // La entrevista y el cambio de estado quedan vinculados en el historial.
            historialPostulanteService.registrarCambioEstado(
                    guardado, estadoAnterior, EstadoPostulante.EN_ENTREVISTA,
                    "Entrevista programada.", registradoPor);
        }
    }

    private void validar(EntrevistaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La entrevista no puede ser nula.");
        }
        if (dto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha de entrevista es obligatoria.");
        }
        if (dto.getHora() == null) {
            throw new IllegalArgumentException("La hora de entrevista es obligatoria.");
        }
        ValidationUtils.validarTextoObligatorio(dto.getLugar(), "El lugar de la entrevista", 150);
        ValidationUtils.validarTextoObligatorio(dto.getModalidad(), "La modalidad de la entrevista", 60);
        ValidationUtils.validarTextoOpcional(dto.getObservacion(), "La observacion de la entrevista", 700);
        if (dto.getPostulanteId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un postulante.");
        }
    }
}
