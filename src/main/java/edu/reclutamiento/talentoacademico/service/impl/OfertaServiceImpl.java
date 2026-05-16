package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.mapper.OfertaMapper;
import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.repository.AreaRepository;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.service.OfertaService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class OfertaServiceImpl implements OfertaService {
    private final OfertaRepository ofertaRepository;
    private final AreaRepository areaRepository;
    private final PostulanteRepository postulanteRepository;

    public OfertaServiceImpl(OfertaRepository ofertaRepository,
                             AreaRepository areaRepository,
                             PostulanteRepository postulanteRepository) {
        this.ofertaRepository = ofertaRepository;
        this.areaRepository = areaRepository;
        this.postulanteRepository = postulanteRepository;
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
        // F1 - ROJO:
        // La prueba inicia exigiendo que una oferta sin titulo sea rechazada con HTTP 400.
        //
        // F2 - VERDE:
        // La implementacion minima podria devolver un DTO con el titulo recibido.
        //
        // F3 - REFACTORIZAR:
        // El codigo final valida reglas de negocio, construye la entidad y persiste con JPA.
        validar(dto);
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

    public OfertaDTO actualizar(Long id, OfertaDTO dto) {
        // F1 - ROJO:
        // La prueba exige 404 cuando se intenta actualizar una oferta inexistente.
        //
        // F2 - VERDE:
        // El cambio minimo modifica el titulo de una oferta existente.
        //
        // F3 - REFACTORIZAR:
        // Se reutiliza repository, validacion y mapper para mantener id y guardar cambios reales.
        OfertaLaboral oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Oferta no encontrada con id " + id));
        validar(dto);
        oferta.setTitulo(dto.getTitulo());
        oferta.setDescripcion(dto.getDescripcion());
        oferta.setVacantes(dto.getVacantes());
        if (dto.getActiva() != null) {
            oferta.setActiva(dto.getActiva());
        }
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findById(dto.getAreaId()).orElse(null);
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

    public void eliminarReal(Long id) {
        // F1 - ROJO:
        // La prueba exige 404 cuando se intenta eliminar una oferta inexistente.
        //
        // F2 - VERDE:
        // El cambio minimo elimina una oferta existente.
        //
        // F3 - REFACTORIZAR:
        // Antes de borrar, se limpian postulantes asociados para respetar la FK de JPA.
        if (!ofertaRepository.existsById(id)) {
            throw new NoSuchElementException("Oferta no encontrada con id " + id);
        }
        // Limpiar primero los postulantes asociados para no violar la FK.
        postulanteRepository.findAll().stream()
                .filter(p -> p.getOferta() != null && id.equals(p.getOferta().getId()))
                .forEach(p -> postulanteRepository.deleteById(p.getId()));
        ofertaRepository.deleteById(id);
    }

    private void validar(OfertaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula.");
        }
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (dto.getVacantes() == null || dto.getVacantes() <= 0) {
            throw new IllegalArgumentException("Las vacantes deben ser mayores a 0.");
        }
    }
}
