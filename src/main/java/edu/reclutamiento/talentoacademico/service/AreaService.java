package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.model.Area;
import java.util.List;

public interface AreaService {
    List<Area> listar();
    Area buscar(Long id);
    Area guardar(Area area);
    void eliminar(Long id);
}
