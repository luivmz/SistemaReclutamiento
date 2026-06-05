package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.repository.AreaRepository;
import edu.reclutamiento.talentoacademico.service.AreaService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;

    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> listar() {
        return areaRepository.findAll();
    }

    public List<Area> listarActivas() {
        // Las areas inactivas no se ofrecen al publico ni en formularios de nuevas ofertas.
        // Spring Data interpreta findByActivaTrue y genera el filtro por activa = true.
        return areaRepository.findByActivaTrue();
    }

    public Area buscar(Long id) {
        return areaRepository.findById(id).orElse(new Area());
    }

    public Area guardar(Area area) {
        validar(area);
        area.setNombre(ValidationUtils.normalizar(area.getNombre()));
        area.setDescripcion(ValidationUtils.normalizar(area.getDescripcion()));
        area.setActiva(area.getActiva() == null || area.getActiva());
        return areaRepository.save(area);
    }

    public void activar(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Area no encontrada con id " + id));
        area.setActiva(true);
        areaRepository.save(area);
    }

    public void eliminar(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Area no encontrada con id " + id));
        area.setActiva(false);
        areaRepository.save(area);
    }

    private void validar(Area area) {
        if (area == null) {
            throw new IllegalArgumentException("El area no puede ser nula.");
        }
        ValidationUtils.validarNombre(area.getNombre(), "El nombre del area");
        ValidationUtils.validarTextoOpcional(area.getDescripcion(), "La descripcion del area", 255);
    }
}
