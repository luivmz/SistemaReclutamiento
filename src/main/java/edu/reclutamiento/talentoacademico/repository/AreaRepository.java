package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.Area;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByActivaTrue();
}
