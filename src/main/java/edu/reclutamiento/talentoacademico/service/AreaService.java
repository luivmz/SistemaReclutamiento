package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.model.Area;
import java.util.List;

public interface AreaService {
    List<Area> listar();
    Area guardar(Area area);
}
