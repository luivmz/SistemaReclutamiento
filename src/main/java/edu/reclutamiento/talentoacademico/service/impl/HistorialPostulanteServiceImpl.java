package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.HistorialPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.repository.HistorialPostulanteRepository;
import edu.reclutamiento.talentoacademico.service.HistorialPostulanteService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HistorialPostulanteServiceImpl implements HistorialPostulanteService {
    private final HistorialPostulanteRepository historialRepository;

    public HistorialPostulanteServiceImpl(HistorialPostulanteRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public List<HistorialPostulante> listar() {
        return historialRepository.findAllByOrderByFechaCambioDesc();
    }

    public List<HistorialPostulante> listarPorPostulante(Long postulanteId) {
        return historialRepository.findByPostulanteIdOrderByFechaCambioDesc(postulanteId);
    }

    public void registrarCambioEstado(Postulante postulante, EstadoPostulante estadoAnterior,
                                      EstadoPostulante estadoNuevo, String observacion, String usuarioActual) {
        if (postulante == null || estadoNuevo == null || estadoAnterior == estadoNuevo) {
            return;
        }

        // Este save participa en la transaccion del servicio que realizo el cambio de estado.
        HistorialPostulante historial = new HistorialPostulante();
        historial.setPostulante(postulante);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setFechaCambio(LocalDateTime.now());
        historial.setObservacion(observacion);
        historial.setRegistradoPor(usuarioActual);
        historialRepository.save(historial);
    }
}
