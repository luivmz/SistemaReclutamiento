package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.Pregunta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByOfertaId(Long ofertaId);
}
