package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.mapper.EntrevistaMapper;
import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
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
        boolean esNueva = dto.getId() == null;
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
            marcarPostulanteEnEntrevista(postulante);
        }

        return EntrevistaMapper.toDTO(guardada);
    }

    public void eliminar(Long id) {
        entrevistaRepository.deleteById(id);
    }

    private void validarPostulanteActivo(Postulante postulante) {
        EstadoPostulante estado = postulante.getEstado();
        if (estado == EstadoPostulante.APROBADO || estado == EstadoPostulante.RECHAZADO) {
            throw new IllegalStateException("Solo se puede programar entrevista a un postulante activo.");
        }
    }

    private void marcarPostulanteEnEntrevista(Postulante postulante) {
        if (postulante.getEstado() != EstadoPostulante.APROBADO
                && postulante.getEstado() != EstadoPostulante.RECHAZADO) {
            postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
            postulanteRepository.save(postulante);
        }
    }
}
