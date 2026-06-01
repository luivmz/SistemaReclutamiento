package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.model.Area;
import java.util.List;

public interface AreaService {
    List<Area> listar();
    List<Area> listarActivas();
    Area buscar(Long id);
    Area guardar(Area area);
    void activar(Long id);
    void eliminar(Long id);
}
