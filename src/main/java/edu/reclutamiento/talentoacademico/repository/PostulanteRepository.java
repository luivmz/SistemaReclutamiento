package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.Postulante;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulanteRepository extends JpaRepository<Postulante, Long> {
    List<Postulante> findByEstado(EstadoPostulante estado);
    List<Postulante> findByEstadoIn(Collection<EstadoPostulante> estados);
    List<Postulante> findByEmail(String email);
}
