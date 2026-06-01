package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfertaRepository extends JpaRepository<OfertaLaboral, Long> {
    List<OfertaLaboral> findByActivaTrue();
    List<OfertaLaboral> findByActivaTrueAndAreaActivaTrue();
}
