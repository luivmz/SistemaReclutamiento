package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.EstadoResultado;
import edu.reclutamiento.talentoacademico.model.ResultadoEntrevista;
import edu.reclutamiento.talentoacademico.service.ResultadoEntrevistaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResultadoEntrevistaController {

    private final ResultadoEntrevistaService resultadoService;

    public ResultadoEntrevistaController(ResultadoEntrevistaService resultadoService) {
        this.resultadoService = resultadoService;
    }

    @GetMapping("/admin/resultados-entrevista")
    public String listar(Model model) {
        model.addAttribute("resultados", resultadoService.listar());
        return "admin/resultados-entrevista";
    }

    @GetMapping("/admin/resultados-entrevista/nuevo/{entrevistaId}")
    public String nuevo(@PathVariable Long entrevistaId, Model model,
                        RedirectAttributes redirectAttributes) {
        try {
            ResultadoEntrevista resultado = resultadoService.preparar(entrevistaId);
            if (resultado.getId() != null) {
                return "redirect:/admin/resultados-entrevista/editar/" + resultado.getId();
            }
            model.addAttribute("resultado", resultado);
            return "admin/resultado-entrevista-form";
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/entrevistas";
        }
    }

    @GetMapping("/admin/resultados-entrevista/editar/{id}")
    public String editar(@PathVariable Long id, Model model,
                         RedirectAttributes redirectAttributes) {
        ResultadoEntrevista resultado = resultadoService.buscar(id);
        if (resultado == null) {
            return "redirect:/admin/resultados-entrevista";
        }
        if (resultado.getEntrevista().getEstadoEntrevista() == EstadoEntrevista.CANCELADA
                || resultado.getEntrevista().getPostulante().getEstado() == EstadoPostulante.CANCELADO) {
            redirectAttributes.addFlashAttribute(
                    "error", "No se puede editar el resultado de un proceso cancelado.");
            return "redirect:/admin/entrevistas";
        }
        model.addAttribute("resultado", resultado);
        return "admin/resultado-entrevista-form";
    }

    @PostMapping("/admin/resultados-entrevista/guardar")
    public String guardar(@RequestParam(required = false) Long id,
                          @RequestParam Long entrevistaId,
                          @RequestParam String resultado,
                          @RequestParam(required = false) Integer puntaje,
                          @RequestParam(required = false) String observacion,
                          @RequestParam(required = false) String recomendacion,
                          HttpSession session,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            ResultadoEntrevista entidad;
            // En edicion se intenta conservar la entidad existente; si no aparece, se arma una nueva.
            if (id != null) {
                entidad = resultadoService.buscar(id);
                if (entidad == null) {
                    entidad = new ResultadoEntrevista();
                }
            } else {
                entidad = new ResultadoEntrevista();
            }

            // Solo se necesita el id para que el servicio busque la entrevista real y valide su existencia.
            Entrevista entrevista = new Entrevista();
            entrevista.setId(entrevistaId);
            entidad.setEntrevista(entrevista);
            entidad.setResultado(EstadoResultado.valueOf(resultado));
            entidad.setPuntaje(puntaje);
            entidad.setObservacion(observacion);
            entidad.setRecomendacion(recomendacion);

            String registradoPor = (String) session.getAttribute("nombre");
            resultadoService.guardar(entidad, registradoPor);
            return "redirect:/admin/resultados-entrevista";
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/entrevistas";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            // Se repuebla el resultado con lo enviado para que el usuario no pierda los datos del formulario.
            model.addAttribute("resultado", recuperarResultadoConDatos(id, entrevistaId, resultado, puntaje, observacion, recomendacion));
            return "admin/resultado-entrevista-form";
        }
    }

    @GetMapping("/admin/resultados-entrevista/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        resultadoService.eliminar(id);
        return "redirect:/admin/resultados-entrevista";
    }

    private ResultadoEntrevista recuperarResultadoConDatos(Long id, Long entrevistaId, String resultado,
                                                           Integer puntaje, String observacion, String recomendacion) {
        ResultadoEntrevista r = new ResultadoEntrevista();
        r.setId(id);
        if (entrevistaId != null) {
            // preparar trae la entrevista asociada aunque el resultado aun no haya sido guardado.
            ResultadoEntrevista existente = resultadoService.preparar(entrevistaId);
            r.setEntrevista(existente.getEntrevista());
        }
        if (resultado != null && !resultado.isBlank()) {
            try {
                r.setResultado(EstadoResultado.valueOf(resultado));
            } catch (IllegalArgumentException ex) {
                // Si llega un valor invalido desde el request, la vista vuelve a un estado seguro.
                r.setResultado(EstadoResultado.PENDIENTE);
            }
        }
        r.setPuntaje(puntaje);
        r.setObservacion(observacion);
        r.setRecomendacion(recomendacion);
        return r;
    }
}
