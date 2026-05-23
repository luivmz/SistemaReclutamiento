package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.ResultadoEntrevista;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoEntrevistaRepository extends JpaRepository<ResultadoEntrevista, Long> {
    Optional<ResultadoEntrevista> findByEntrevistaId(Long entrevistaId);
    boolean existsByEntrevistaId(Long entrevistaId);
}
