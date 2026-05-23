package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.mapper.EntrevistaMapper;
import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.model.ResultadoEntrevista;
import edu.reclutamiento.talentoacademico.repository.EntrevistaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.service.EntrevistaService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntrevistaServiceImpl implements EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;
    private final PostulanteRepository postulanteRepository;

    public EntrevistaServiceImpl(EntrevistaRepository entrevistaRepository, PostulanteRepository postulanteRepository) {
        this.entrevistaRepository = entrevistaRepository;
        this.postulanteRepository = postulanteRepository;
    }

    @Transactional(readOnly = true)
    public List<EntrevistaDTO> listar() {
        return entrevistaRepository.findAll().stream().map(EntrevistaMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EntrevistaDTO> listarPorPostulante(Long postulanteId) {
        return entrevistaRepository.findByPostulanteId(postulanteId).stream().map(EntrevistaMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public EntrevistaDTO buscar(Long id) {
        return entrevistaRepository.findById(id).map(EntrevistaMapper::toDTO).orElse(new EntrevistaDTO());
    }

    public EntrevistaDTO guardar(EntrevistaDTO dto) {
        Entrevista entrevista = EntrevistaMapper.toEntity(dto);
        Postulante postulante = null;
        if (dto.getPostulanteId() != null) {
            postulante = postulanteRepository.findById(dto.getPostulanteId()).orElse(null);
            entrevista.setPostulante(postulante);
        }

        if (postulante != null) {
            validarPostulanteActivo(postulante, dto.getId() == null);
        }

        Entrevista guardada = entrevistaRepository.save(entrevista);
        sincronizarEstadoPostulante(guardada);
        return EntrevistaMapper.toDTO(guardada);
    }

    public void eliminar(Long id) {
        entrevistaRepository.deleteById(id);
    }

    private void validarPostulanteActivo(Postulante postulante, boolean esNueva) {
        if (!esNueva) {
            return;
        }
        EstadoPostulante estado = postulante.getEstado();
        if (estado == EstadoPostulante.APROBADO || estado == EstadoPostulante.RECHAZADO) {
            throw new IllegalStateException("Solo se puede programar entrevista a un postulante activo.");
        }
    }

    private void sincronizarEstadoPostulante(Entrevista entrevista) {
        Postulante postulante = entrevista.getPostulante();
        if (postulante == null) {
            return;
        }
        ResultadoEntrevista resultado = entrevista.getResultado();
        if (resultado == ResultadoEntrevista.APROBADO) {
            postulante.setEstado(EstadoPostulante.APROBADO);
            postulante.setAprobado(true);
            postulante.setObservacion("Entrevista aprobada.");
        } else if (resultado == ResultadoEntrevista.DESAPROBADO) {
            postulante.setEstado(EstadoPostulante.RECHAZADO);
            postulante.setAprobado(false);
            postulante.setObservacion("Entrevista desaprobada.");
        } else {
            if (postulante.getEstado() != EstadoPostulante.APROBADO
                    && postulante.getEstado() != EstadoPostulante.RECHAZADO) {
                postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
                postulante.setAprobado(false);
            }
        }
        postulanteRepository.save(postulante);
    }
}
