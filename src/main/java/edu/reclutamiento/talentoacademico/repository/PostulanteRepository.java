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
    List<Postulante> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndOfertaId(Long usuarioId, Long ofertaId);
    long countByEstado(EstadoPostulante estado);
    long countByUsuarioId(Long usuarioId);
    long countByUsuarioIdAndEstado(Long usuarioId, EstadoPostulante estado);
}
