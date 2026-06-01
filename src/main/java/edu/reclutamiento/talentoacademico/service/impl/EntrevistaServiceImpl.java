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
// @Transactional evita que una entrevista quede guardada sin actualizar
// el estado del postulante o sin registrar el movimiento en historial.
@Transactional
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

    // readOnly indica que este metodo solo consulta entrevistas y no modifica la BD.
    @Transactional(readOnly = true)
    public List<EntrevistaDTO> listar() {
        return entrevistaRepository.findAll().stream().map(EntrevistaMapper::toDTO).toList();
    }

    // readOnly evita sincronizaciones innecesarias cuando solo filtramos entrevistas por postulante.
    @Transactional(readOnly = true)
    public List<EntrevistaDTO> listarPorPostulante(Long postulanteId) {
        return entrevistaRepository.findByPostulanteId(postulanteId).stream().map(EntrevistaMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public EntrevistaDTO buscar(Long id) {
        return entrevistaRepository.findById(id).map(EntrevistaMapper::toDTO).orElse(new EntrevistaDTO());
    }

    public EntrevistaDTO guardar(EntrevistaDTO dto, String registradoPor) {
        validar(dto);
        boolean esNueva = dto.getId() == null;
        if (esNueva && dto.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede programar una entrevista con fecha pasada.");
        }
        Entrevista entrevista = EntrevistaMapper.toEntity(dto);
        Postulante postulante = null;
        if (dto.getPostulanteId() != null) {
            postulante = postulanteRepository.findById(dto.getPostulanteId()).orElse(null);
            entrevista.setPostulante(postulante);
        }

        if (postulante != null && esNueva) {
            validarPostulanteActivo(postulante);
        }

        Entrevista guardada = entrevistaRepository.save(entrevista);

        if (postulante != null && esNueva) {
            marcarPostulanteEnEntrevista(postulante, registradoPor);
        }

        return EntrevistaMapper.toDTO(guardada);
    }

    public void eliminar(Long id) {
        entrevistaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
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
