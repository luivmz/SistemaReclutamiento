package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.service.EvaluacionService;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EvaluacionController {
    private final EvaluacionService evaluacionService;
    private final PostulanteService postulanteService;

    public EvaluacionController(EvaluacionService evaluacionService, PostulanteService postulanteService) {
        this.evaluacionService = evaluacionService;
        this.postulanteService = postulanteService;
    }

    @GetMapping("/postulante/evaluacion/{postulacionId}")
    public String evaluacion(@PathVariable Long postulacionId, HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (!evaluacionService.puedeRendir(postulacionId, usuarioId)) {
            return "redirect:/acceso-denegado";
        }
        postulanteService.marcarEnEvaluacion(postulacionId);
        model.addAttribute("postulacion", postulanteService.buscar(postulacionId));
        model.addAttribute("preguntas", evaluacionService.listarPreguntasPorPostulacion(postulacionId));
        return "postulante/evaluacion";
    }

    @PostMapping("/postulante/evaluacion/{postulacionId}")
    public String calificar(@PathVariable Long postulacionId, @RequestParam Map<String, String> respuestas,
                            HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        int puntaje = evaluacionService.calificar(postulacionId, usuarioId, respuestas);
        PostulanteDTO postulacion = postulanteService.buscar(postulacionId);
        model.addAttribute("puntaje", puntaje);
        model.addAttribute("postulacion", postulacion);
        return "postulante/resultado-evaluacion";
    }
}
