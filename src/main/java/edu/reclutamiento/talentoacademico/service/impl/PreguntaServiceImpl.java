package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.PreguntaDTO;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.model.Pregunta;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PreguntaRepository;
import edu.reclutamiento.talentoacademico.service.PreguntaService;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreguntaServiceImpl implements PreguntaService {
    private final PreguntaRepository preguntaRepository;
    private final OfertaRepository ofertaRepository;

    public PreguntaServiceImpl(PreguntaRepository preguntaRepository, OfertaRepository ofertaRepository) {
        this.preguntaRepository = preguntaRepository;
        this.ofertaRepository = ofertaRepository;
    }

    @Transactional(readOnly = true)
    public List<PreguntaDTO> listar() {
        return preguntaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public PreguntaDTO guardar(PreguntaDTO dto) {
        Pregunta pregunta = new Pregunta();
        pregunta.setId(dto.getId());
        pregunta.setEnunciado(dto.getEnunciado());
        pregunta.setOpcionCorrecta(dto.getOpcionCorrecta());
        if (dto.getOfertaIds() != null && !dto.getOfertaIds().isEmpty()) {
            pregunta.setOfertas(new LinkedHashSet<>(ofertaRepository.findAllById(dto.getOfertaIds())));
        }
        return toDTO(preguntaRepository.save(pregunta));
    }

    private PreguntaDTO toDTO(Pregunta pregunta) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setId(pregunta.getId());
        dto.setEnunciado(pregunta.getEnunciado());
        dto.setOpcionCorrecta(pregunta.getOpcionCorrecta());
        if (pregunta.getOfertas() != null && !pregunta.getOfertas().isEmpty()) {
            dto.setOfertaIds(pregunta.getOfertas().stream().map(OfertaLaboral::getId).toList());
            dto.setOfertasTitulos(String.join(", ",
                    pregunta.getOfertas().stream().map(OfertaLaboral::getTitulo).toList()));
        }
        return dto;
    }
}
