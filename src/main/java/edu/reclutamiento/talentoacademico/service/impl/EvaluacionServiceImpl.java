package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.model.Pregunta;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.repository.PreguntaRepository;
import edu.reclutamiento.talentoacademico.service.EvaluacionService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EvaluacionServiceImpl implements EvaluacionService {
    private final PostulanteRepository postulanteRepository;
    private final PreguntaRepository preguntaRepository;

    public EvaluacionServiceImpl(PostulanteRepository postulanteRepository, PreguntaRepository preguntaRepository) {
        this.postulanteRepository = postulanteRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Transactional(readOnly = true)
    public List<Pregunta> listarPreguntasPorPostulacion(Long postulacionId) {
        Postulante postulante = postulanteRepository.findById(postulacionId).orElseThrow();
        return preguntaRepository.findByOfertasId(postulante.getOferta().getId());
    }

    @Transactional(readOnly = true)
    public boolean puedeRendir(Long postulacionId, Long usuarioId) {
        Postulante postulante = postulanteRepository.findById(postulacionId).orElse(null);
        if (postulante == null || postulante.getUsuario() == null || !postulante.getUsuario().getId().equals(usuarioId)) {
            return false;
        }
        return postulante.getEstado() == EstadoPostulante.POSTULADO
                || postulante.getEstado() == EstadoPostulante.EN_EVALUACION;
    }

    public int calificar(Long postulacionId, Long usuarioId, Map<String, String> respuestas) {
        if (!puedeRendir(postulacionId, usuarioId)) {
            throw new IllegalStateException("No puedes rendir esta evaluacion.");
        }
        Postulante postulante = postulanteRepository.findById(postulacionId).orElseThrow();
        List<Pregunta> preguntas = preguntaRepository.findByOfertasId(postulante.getOferta().getId());

        int correctas = 0;
        for (Pregunta pregunta : preguntas) {
            String respuesta = respuestas.get("respuesta_" + pregunta.getId());
            if (respuesta != null && respuesta.trim().equalsIgnoreCase(pregunta.getOpcionCorrecta())) {
                correctas++;
            }
        }

        int puntaje = preguntas.isEmpty() ? 0 : (correctas * 100) / preguntas.size();
        boolean aprobado = puntaje >= 60;
        postulante.setPuntaje(puntaje);
        postulante.setAprobado(aprobado);
        postulante.setEstado(aprobado ? EstadoPostulante.APROBADO : EstadoPostulante.RECHAZADO);
        postulante.setObservacion(aprobado ? "Evaluacion aprobada." : "Evaluacion desaprobada.");
        postulanteRepository.save(postulante);
        return puntaje;
    }
}
