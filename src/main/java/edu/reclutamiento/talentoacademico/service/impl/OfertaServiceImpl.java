package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.mapper.OfertaMapper;
import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.repository.AreaRepository;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OfertaServiceImpl implements OfertaService {
    private final OfertaRepository ofertaRepository;
    private final AreaRepository areaRepository;

    public OfertaServiceImpl(OfertaRepository ofertaRepository, AreaRepository areaRepository) {
        this.ofertaRepository = ofertaRepository;
        this.areaRepository = areaRepository;
    }

    public List<OfertaDTO> listar() {
        return ofertaRepository.findAll().stream().map(OfertaMapper::toDTO).toList();
    }

    public List<OfertaDTO> listarActivas() {
        return ofertaRepository.findByActivaTrue().stream().map(OfertaMapper::toDTO).toList();
    }

    public OfertaDTO buscar(Long id) {
        return ofertaRepository.findById(id).map(OfertaMapper::toDTO).orElse(null);
    }

    public OfertaDTO guardar(OfertaDTO dto) {
        OfertaLaboral oferta = new OfertaLaboral();
        oferta.setId(dto.getId());
        oferta.setTitulo(dto.getTitulo());
        oferta.setDescripcion(dto.getDescripcion());
        oferta.setVacantes(dto.getVacantes());
        oferta.setActiva(dto.getActiva() == null || dto.getActiva());
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findById(dto.getAreaId()).orElse(null);
            oferta.setArea(area);
        }
        return OfertaMapper.toDTO(ofertaRepository.save(oferta));
    }

    public void eliminar(Long id) {
        ofertaRepository.deleteById(id);
    }
}
