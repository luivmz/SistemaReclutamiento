package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.EstadoResultado;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.model.ResultadoEntrevista;
import edu.reclutamiento.talentoacademico.repository.EntrevistaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.repository.ResultadoEntrevistaRepository;
import edu.reclutamiento.talentoacademico.service.HistorialPostulanteService;
import edu.reclutamiento.talentoacademico.service.ResultadoEntrevistaService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResultadoEntrevistaServiceImpl implements ResultadoEntrevistaService {

    private final ResultadoEntrevistaRepository resultadoRepository;
    private final EntrevistaRepository entrevistaRepository;
    private final PostulanteRepository postulanteRepository;
    private final HistorialPostulanteService historialPostulanteService;

    public ResultadoEntrevistaServiceImpl(ResultadoEntrevistaRepository resultadoRepository,
                                          EntrevistaRepository entrevistaRepository,
                                          PostulanteRepository postulanteRepository,
                                          HistorialPostulanteService historialPostulanteService) {
        this.resultadoRepository = resultadoRepository;
        this.entrevistaRepository = entrevistaRepository;
        this.postulanteRepository = postulanteRepository;
        this.historialPostulanteService = historialPostulanteService;
    }

    public List<ResultadoEntrevista> listar() {
        return resultadoRepository.findAll();
    }

    public ResultadoEntrevista buscar(Long id) {
        return resultadoRepository.findById(id).orElse(null);
    }

    public ResultadoEntrevista buscarPorEntrevista(Long entrevistaId) {
        return resultadoRepository.findByEntrevistaId(entrevistaId).orElse(null);
    }

    public ResultadoEntrevista preparar(Long entrevistaId) {
        ResultadoEntrevista existente = buscarPorEntrevista(entrevistaId);
        if (existente != null) {
            return existente;
        }

        // Si aun no existe resultado, se arma un objeto temporal para precargar el formulario.
        Entrevista entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new IllegalArgumentException("Entrevista no encontrada."));
        validarEntrevistaDisponibleParaResultado(entrevista, true);
        ResultadoEntrevista nuevo = new ResultadoEntrevista();
        nuevo.setEntrevista(entrevista);
        nuevo.setResultado(EstadoResultado.PENDIENTE);
        nuevo.setFechaRegistro(LocalDateTime.now());
        return nuevo;
    }

    // Se usa transaccion porque guardar resultado tambien actualiza entrevista,
    // postulante e historial como una sola operacion.
    @Transactional
    public ResultadoEntrevista guardar(ResultadoEntrevista resultado, String registradoPor) {
        validar(resultado);
        if (resultado.getEntrevista() == null || resultado.getEntrevista().getId() == null) {
            throw new IllegalArgumentException("La entrevista es obligatoria para registrar el resultado.");
        }

        Entrevista entrevista = entrevistaRepository.findById(resultado.getEntrevista().getId())
                .orElseThrow(() -> new IllegalArgumentException("Entrevista no encontrada."));
        resultado.setEntrevista(entrevista);

        // Solo se impide duplicar cuando se esta creando un resultado nuevo.
        // Al editar, el mismo registro puede volver a guardarse.
        boolean esNuevo = resultado.getId() == null;
        validarEntrevistaDisponibleParaResultado(entrevista, esNuevo);
        if (esNuevo && resultadoRepository.existsByEntrevistaId(entrevista.getId())) {
            throw new IllegalStateException("La entrevista ya tiene un resultado registrado.");
        }

        // Valores por defecto para que el formulario pueda guardar resultados incompletos como pendientes.
        if (resultado.getResultado() == null) {
            resultado.setResultado(EstadoResultado.PENDIENTE);
        }
        if (resultado.getFechaRegistro() == null) {
            resultado.setFechaRegistro(LocalDateTime.now());
        }
        if (registradoPor != null && !registradoPor.isBlank()) {
            resultado.setRegistradoPor(registradoPor);
        }

        ResultadoEntrevista guardado = resultadoRepository.save(resultado);

        // Estas actualizaciones dependen del resultado guardado y deben confirmarse juntas.
        actualizarEstadoEntrevista(entrevista);
        actualizarEstadoPostulante(entrevista.getPostulante(), guardado, registradoPor);

        return guardado;
    }

    // Elimina el resultado y restablece los estados relacionados dentro de la misma transaccion.
    @Transactional
    public void eliminar(Long id) {
        ResultadoEntrevista existente = resultadoRepository.findById(id).orElse(null);
        if (existente == null) {
            return;
        }
        Entrevista entrevista = existente.getEntrevista();
        resultadoRepository.delete(existente);

        if (entrevista != null) {
            // Al eliminar el resultado, la entrevista vuelve a quedar pendiente de ejecucion.
            entrevista.setEstadoEntrevista(EstadoEntrevista.PROGRAMADA);
            entrevistaRepository.save(entrevista);

            Postulante postulante = entrevista.getPostulante();
            // Solo se devuelve a EN_ENTREVISTA si el postulante no habia llegado ya a un estado final.
            if (postulante != null
                    && postulante.getEstado() != EstadoPostulante.APROBADO
                    && postulante.getEstado() != EstadoPostulante.RECHAZADO
                    && postulante.getEstado() != EstadoPostulante.CANCELADO) {
                postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
                postulanteRepository.save(postulante);
            }
        }
    }

    private void actualizarEstadoEntrevista(Entrevista entrevista) {
        entrevista.setEstadoEntrevista(EstadoEntrevista.REALIZADA);
        entrevistaRepository.save(entrevista);
    }

    private void validarEntrevistaDisponibleParaResultado(Entrevista entrevista, boolean esNuevo) {
        // Las entrevistas canceladas no pueden recibir resultados.
        if (entrevista.getEstadoEntrevista() == EstadoEntrevista.CANCELADA) {
            throw new IllegalStateException("No se puede registrar resultado en una entrevista cancelada.");
        }

        Postulante postulante = entrevista.getPostulante();
        if (postulante == null) {
            throw new IllegalStateException("La entrevista no tiene un postulante asociado.");
        }
        if (postulante.getEstado() == EstadoPostulante.CANCELADO) {
            throw new IllegalStateException("No se puede evaluar una postulacion cancelada.");
        }
        if (postulante.getEstado() == EstadoPostulante.RECHAZADO) {
            throw new IllegalStateException("No se puede cambiar el resultado de un postulante rechazado.");
        }
        if (esNuevo && postulante.getEstado() == EstadoPostulante.APROBADO) {
            throw new IllegalStateException("La postulacion ya tiene un estado final.");
        }
    }

    private void actualizarEstadoPostulante(Postulante postulante, ResultadoEntrevista resultadoGuardado,
                                            String registradoPor) {
        if (postulante == null) {
            return;
        }
        EstadoPostulante estadoAnterior = postulante.getEstado();
        EstadoResultado resultado = resultadoGuardado.getResultado();
        String observacionHistorial = null;

        if (resultado == EstadoResultado.DESAPROBADO) {
            // Si desaprueba cualquier entrevista, el proceso termina como RECHAZADO.
            String tipoEntrevista = resultadoGuardado.getEntrevista().getTipoEntrevista().name();
            String observacion = "Postulante rechazado por desaprobar entrevista " + tipoEntrevista + ".";
            postulante.setEstado(EstadoPostulante.RECHAZADO);
            postulante.setAprobado(false);
            postulante.setObservacion(observacion);
            observacionHistorial = observacion;
        } else if (resultado == EstadoResultado.APROBADO && todasLasEntrevistasActivasAprobadas(postulante, resultadoGuardado)) {
            postulante.setEstado(EstadoPostulante.APROBADO);
            postulante.setAprobado(true);
            postulante.setObservacion("Entrevistas aprobadas.");
            observacionHistorial = "Entrevistas aprobadas.";
        } else {
            postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
            postulante.setAprobado(false);
            observacionHistorial = resultado == EstadoResultado.APROBADO
                    ? "Entrevista aprobada. Pendiente completar entrevistas."
                    : "Resultado pendiente.";
        }
        Postulante guardado = postulanteRepository.save(postulante);
        // El historial permite auditar por que cambio el estado del postulante.
        historialPostulanteService.registrarCambioEstado(
                guardado, estadoAnterior, guardado.getEstado(), observacionHistorial, registradoPor);
    }

    private boolean todasLasEntrevistasActivasAprobadas(Postulante postulante, ResultadoEntrevista resultadoActual) {
        List<Entrevista> entrevistas = entrevistaRepository.findByPostulanteId(postulante.getId()).stream()
                .filter(entrevista -> entrevista.getEstadoEntrevista() != EstadoEntrevista.CANCELADA)
                .toList();

        return !entrevistas.isEmpty() && entrevistas.stream().allMatch(entrevista -> {
            ResultadoEntrevista resultado = entrevista.getId().equals(resultadoActual.getEntrevista().getId())
                    ? resultadoActual
                    : resultadoRepository.findByEntrevistaId(entrevista.getId()).orElse(null);
            return resultado != null && resultado.getResultado() == EstadoResultado.APROBADO;
        });
    }

    private void validar(ResultadoEntrevista resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("El resultado no puede ser nulo.");
        }
        if (resultado.getResultado() == null) {
            resultado.setResultado(EstadoResultado.PENDIENTE);
        }
        // El puntaje se limita entre 0 y 100 para evitar datos invalidos.
        if (resultado.getPuntaje() != null && (resultado.getPuntaje() < 0 || resultado.getPuntaje() > 100)) {
            throw new IllegalArgumentException("El puntaje debe estar entre 0 y 100.");
        }
        // Los resultados finales requieren sustento; los pendientes pueden guardarse sin observacion.
        if (resultado.getResultado() != EstadoResultado.PENDIENTE) {
            ValidationUtils.validarTextoObligatorio(resultado.getObservacion(), "La observacion del resultado", 700);
        } else {
            ValidationUtils.validarTextoOpcional(resultado.getObservacion(), "La observacion del resultado", 700);
        }
        ValidationUtils.validarTextoOpcional(resultado.getRecomendacion(), "La recomendacion", 700);
    }
}
