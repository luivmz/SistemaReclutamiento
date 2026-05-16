package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.model.Pregunta;
import java.util.List;
import java.util.Map;

public interface EvaluacionService {
    List<Pregunta> listarPreguntasPorPostulacion(Long postulacionId);
    boolean puedeRendir(Long postulacionId, Long usuarioId);
    int calificar(Long postulacionId, Long usuarioId, Map<String, String> respuestas);
}
