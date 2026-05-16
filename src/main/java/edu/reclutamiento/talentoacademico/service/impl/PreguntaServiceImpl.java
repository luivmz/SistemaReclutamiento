package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.PreguntaDTO;
import edu.reclutamiento.talentoacademico.model.Pregunta;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PreguntaRepository;
import edu.reclutamiento.talentoacademico.service.PreguntaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PreguntaServiceImpl implements PreguntaService {
    private final PreguntaRepository preguntaRepository;
    private final OfertaRepository ofertaRepository;

    public PreguntaServiceImpl(PreguntaRepository preguntaRepository, OfertaRepository ofertaRepository) {
        this.preguntaRepository = preguntaRepository;
        this.ofertaRepository = ofertaRepository;
    }

    public List<PreguntaDTO> listar() {
        return preguntaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public PreguntaDTO guardar(PreguntaDTO dto) {
        Pregunta pregunta = new Pregunta();
        pregunta.setId(dto.getId());
        pregunta.setEnunciado(dto.getEnunciado());
        pregunta.setOpcionCorrecta(dto.getOpcionCorrecta());
        if (dto.getOfertaId() != null) {
            pregunta.setOferta(ofertaRepository.findById(dto.getOfertaId()).orElse(null));
        }
        return toDTO(preguntaRepository.save(pregunta));
    }

    private PreguntaDTO toDTO(Pregunta pregunta) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setId(pregunta.getId());
        dto.setEnunciado(pregunta.getEnunciado());
        dto.setOpcionCorrecta(pregunta.getOpcionCorrecta());
        if (pregunta.getOferta() != null) {
            dto.setOfertaId(pregunta.getOferta().getId());
            dto.setOfertaTitulo(pregunta.getOferta().getTitulo());
        }
        return dto;
    }
}
