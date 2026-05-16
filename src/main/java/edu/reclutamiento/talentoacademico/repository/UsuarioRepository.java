package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndPasswordAndActivoTrue(String email, String password);
}
