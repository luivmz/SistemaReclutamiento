package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.Entrevista;
import edu.reclutamiento.talentoacademico.model.EstadoEntrevista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {
    List<Entrevista> findByPostulanteId(Long postulanteId);
    long countByEstadoEntrevista(EstadoEntrevista estadoEntrevista);
}
