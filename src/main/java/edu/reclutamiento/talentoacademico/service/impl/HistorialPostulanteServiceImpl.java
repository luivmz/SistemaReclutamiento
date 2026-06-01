package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.HistorialPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.repository.HistorialPostulanteRepository;
import edu.reclutamiento.talentoacademico.service.HistorialPostulanteService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// @Transactional asegura que cada movimiento de historial se guarde de forma consistente
// cuando un servicio cambia el estado de un postulante.
@Transactional
public class HistorialPostulanteServiceImpl implements HistorialPostulanteService {
    private final HistorialPostulanteRepository historialRepository;

    public HistorialPostulanteServiceImpl(HistorialPostulanteRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    // readOnly se usa porque el historial solo se consulta en esta pantalla.
    @Transactional(readOnly = true)
    public List<HistorialPostulante> listar() {
        return historialRepository.findAllByOrderByFechaCambioDesc();
    }

    @Transactional(readOnly = true)
    public List<HistorialPostulante> listarPorPostulante(Long postulanteId) {
        return historialRepository.findByPostulanteIdOrderByFechaCambioDesc(postulanteId);
    }

    public void registrarCambioEstado(Postulante postulante, EstadoPostulante estadoAnterior,
                                      EstadoPostulante estadoNuevo, String observacion, String usuarioActual) {
        if (postulante == null || estadoNuevo == null || estadoAnterior == estadoNuevo) {
            return;
        }

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
