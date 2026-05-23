package edu.reclutamiento.talentoacademico.service;

import edu.reclutamiento.talentoacademico.model.ResultadoEntrevista;
import java.util.List;

public interface ResultadoEntrevistaService {
    List<ResultadoEntrevista> listar();
    ResultadoEntrevista buscar(Long id);
    ResultadoEntrevista buscarPorEntrevista(Long entrevistaId);
    ResultadoEntrevista preparar(Long entrevistaId);
    ResultadoEntrevista guardar(ResultadoEntrevista resultado, String registradoPor);
    void eliminar(Long id);
}
