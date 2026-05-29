package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.HistorialPostulante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialPostulanteRepository extends JpaRepository<HistorialPostulante, Long> {
    List<HistorialPostulante> findAllByOrderByFechaCambioDesc();
    List<HistorialPostulante> findByPostulanteIdOrderByFechaCambioDesc(Long postulanteId);
}
