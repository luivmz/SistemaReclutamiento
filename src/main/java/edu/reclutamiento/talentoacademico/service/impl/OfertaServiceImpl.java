package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.mapper.OfertaMapper;
import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.repository.AreaRepository;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class OfertaServiceImpl implements OfertaService {
    private final OfertaRepository ofertaRepository;
    private final AreaRepository areaRepository;

    public OfertaServiceImpl(OfertaRepository ofertaRepository,
                             AreaRepository areaRepository) {
        this.ofertaRepository = ofertaRepository;
        this.areaRepository = areaRepository;
    }

    public List<OfertaDTO> listar() {
        return ofertaRepository.findAll().stream().map(OfertaMapper::toDTO).toList();
    }

    public List<OfertaDTO> listarActivas() {
        // El listado publico solo muestra ofertas activas y con area activa.
        return ofertaRepository.findByActivaTrueAndAreaActivaTrue().stream().map(OfertaMapper::toDTO).toList();
    }

    public OfertaDTO buscar(Long id) {
        return ofertaRepository.findById(id).map(OfertaMapper::toDTO).orElse(null);
    }

    public OfertaDTO buscarActiva(Long id) {
        return ofertaRepository.findById(id)
                .filter(oferta -> Boolean.TRUE.equals(oferta.getActiva()))
                .filter(oferta -> oferta.getArea() == null || Boolean.TRUE.equals(oferta.getArea().getActiva()))
                .map(OfertaMapper::toDTO)
                .orElse(null);
    }

    public OfertaDTO guardar(OfertaDTO dto) {
        validar(dto);
        OfertaLaboral oferta = new OfertaLaboral();
        oferta.setId(dto.getId());
        oferta.setTitulo(ValidationUtils.normalizar(dto.getTitulo()));
        oferta.setDescripcion(ValidationUtils.normalizar(dto.getDescripcion()));
        oferta.setVacantes(dto.getVacantes());
        oferta.setActiva(dto.getActiva() == null || dto.getActiva());
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findById(dto.getAreaId()).orElse(null);
            if (area != null && !Boolean.TRUE.equals(area.getActiva())) {
                throw new IllegalArgumentException("No se puede asignar una oferta a un area inactiva.");
            }
            oferta.setArea(area);
        }
        return OfertaMapper.toDTO(ofertaRepository.save(oferta));
    }

    public void activar(Long id) {
        OfertaLaboral oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada con id " + id));
        oferta.setActiva(true);
        ofertaRepository.save(oferta);
    }

    public void eliminar(Long id) {
        OfertaLaboral oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada con id " + id));
        oferta.setActiva(false);
        ofertaRepository.save(oferta);
    }

    private void validar(OfertaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula.");
        }
        ValidationUtils.validarNombre(dto.getTitulo(), "El titulo de la oferta");
        ValidationUtils.validarTextoObligatorio(dto.getDescripcion(), "La descripcion de la oferta", 800);
        if (dto.getVacantes() == null) {
            throw new IllegalArgumentException("Las vacantes son obligatorias.");
        }
        ValidationUtils.validarEnteroNoNegativo(dto.getVacantes(), "Las vacantes");
        if (dto.getAreaId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un area academica.");
        }
    }
}
