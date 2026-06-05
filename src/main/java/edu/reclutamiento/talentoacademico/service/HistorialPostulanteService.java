package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.HistorialPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import java.util.List;

public interface HistorialPostulanteService {
    List<HistorialPostulante> listar();
    List<HistorialPostulante> listarPorPostulante(Long postulanteId);
    void registrarCambioEstado(Postulante postulante, EstadoPostulante estadoAnterior,
                                EstadoPostulante estadoNuevo, String observacion, String usuarioActual);
    void registrarEvento(Postulante postulante, String observacion, String usuarioActual);
}
