package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.EntrevistaDTO;
import edu.reclutamiento.talentoacademico.mapper.EntrevistaMapper;
import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.repository.EntrevistaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.service.EntrevistaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EntrevistaServiceImpl implements EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;
    private final PostulanteRepository postulanteRepository;

    public EntrevistaServiceImpl(EntrevistaRepository entrevistaRepository, PostulanteRepository postulanteRepository) {
        this.entrevistaRepository = entrevistaRepository;
        this.postulanteRepository = postulanteRepository;
    }

    public List<EntrevistaDTO> listar() {
        return entrevistaRepository.findAll().stream().map(EntrevistaMapper::toDTO).toList();
    }

    public List<EntrevistaDTO> listarPorPostulante(Long postulanteId) {
        return entrevistaRepository.findByPostulanteId(postulanteId).stream().map(EntrevistaMapper::toDTO).toList();
    }

    public EntrevistaDTO buscar(Long id) {
        return entrevistaRepository.findById(id).map(EntrevistaMapper::toDTO).orElse(new EntrevistaDTO());
    }

    public EntrevistaDTO guardar(EntrevistaDTO dto) {
        Entrevista entrevista = EntrevistaMapper.toEntity(dto);
        if (dto.getPostulanteId() != null) {
            entrevista.setPostulante(postulanteRepository.findById(dto.getPostulanteId()).orElse(null));
        }
        return EntrevistaMapper.toDTO(entrevistaRepository.save(entrevista));
    }

    public void eliminar(Long id) {
        entrevistaRepository.deleteById(id);
    }
}
