package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.repository.AreaRepository;
import edu.reclutamiento.talentoacademico.service.AreaService;
import java.util.List;
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

    public Area guardar(Area area) {
        return areaRepository.save(area);
    }
}
