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
import edu.reclutamiento.talentoacademico.service.ResultadoEntrevistaService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResultadoEntrevistaServiceImpl implements ResultadoEntrevistaService {

    private final ResultadoEntrevistaRepository resultadoRepository;
    private final EntrevistaRepository entrevistaRepository;
    private final PostulanteRepository postulanteRepository;

    public ResultadoEntrevistaServiceImpl(ResultadoEntrevistaRepository resultadoRepository,
                                          EntrevistaRepository entrevistaRepository,
                                          PostulanteRepository postulanteRepository) {
        this.resultadoRepository = resultadoRepository;
        this.entrevistaRepository = entrevistaRepository;
        this.postulanteRepository = postulanteRepository;
    }

    @Transactional(readOnly = true)
    public List<ResultadoEntrevista> listar() {
        return resultadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ResultadoEntrevista buscar(Long id) {
        return resultadoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public ResultadoEntrevista buscarPorEntrevista(Long entrevistaId) {
        return resultadoRepository.findByEntrevistaId(entrevistaId).orElse(null);
    }

    public ResultadoEntrevista preparar(Long entrevistaId) {
        ResultadoEntrevista existente = buscarPorEntrevista(entrevistaId);
        if (existente != null) {
            return existente;
        }
        Entrevista entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new IllegalArgumentException("Entrevista no encontrada."));
        ResultadoEntrevista nuevo = new ResultadoEntrevista();
        nuevo.setEntrevista(entrevista);
        nuevo.setResultado(EstadoResultado.PENDIENTE);
        nuevo.setFechaRegistro(LocalDateTime.now());
        return nuevo;
    }

    public ResultadoEntrevista guardar(ResultadoEntrevista resultado, String registradoPor) {
        if (resultado.getEntrevista() == null || resultado.getEntrevista().getId() == null) {
            throw new IllegalArgumentException("La entrevista es obligatoria para registrar el resultado.");
        }

        Entrevista entrevista = entrevistaRepository.findById(resultado.getEntrevista().getId())
                .orElseThrow(() -> new IllegalArgumentException("Entrevista no encontrada."));
        resultado.setEntrevista(entrevista);

        boolean esNuevo = resultado.getId() == null;
        if (esNuevo && resultadoRepository.existsByEntrevistaId(entrevista.getId())) {
            throw new IllegalStateException("La entrevista ya tiene un resultado registrado.");
        }

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

        actualizarEstadoEntrevista(entrevista);
        actualizarEstadoPostulante(entrevista.getPostulante(), guardado.getResultado());

        return guardado;
    }

    public void eliminar(Long id) {
        ResultadoEntrevista existente = resultadoRepository.findById(id).orElse(null);
        if (existente == null) {
            return;
        }
        Entrevista entrevista = existente.getEntrevista();
        resultadoRepository.delete(existente);

        if (entrevista != null) {
            entrevista.setEstadoEntrevista(EstadoEntrevista.PROGRAMADA);
            entrevistaRepository.save(entrevista);

            Postulante postulante = entrevista.getPostulante();
            if (postulante != null
                    && postulante.getEstado() != EstadoPostulante.APROBADO
                    && postulante.getEstado() != EstadoPostulante.RECHAZADO) {
                postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
                postulanteRepository.save(postulante);
            }
        }
    }

    private void actualizarEstadoEntrevista(Entrevista entrevista) {
        entrevista.setEstadoEntrevista(EstadoEntrevista.REALIZADA);
        entrevistaRepository.save(entrevista);
    }

    private void actualizarEstadoPostulante(Postulante postulante, EstadoResultado resultado) {
        if (postulante == null) {
            return;
        }
        if (resultado == EstadoResultado.APROBADO) {
            postulante.setEstado(EstadoPostulante.APROBADO);
            postulante.setAprobado(true);
            postulante.setObservacion("Entrevista aprobada.");
        } else if (resultado == EstadoResultado.DESAPROBADO) {
            postulante.setEstado(EstadoPostulante.RECHAZADO);
            postulante.setAprobado(false);
            postulante.setObservacion("Entrevista desaprobada.");
        } else {
            postulante.setEstado(EstadoPostulante.EN_ENTREVISTA);
        }
        postulanteRepository.save(postulante);
    }
}
