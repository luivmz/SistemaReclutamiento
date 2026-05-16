package edu.reclutamiento.talentoacademico.repository;

import edu.reclutamiento.talentoacademico.model.Usuario;
import edu.reclutamiento.talentoacademico.model.RolUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndPasswordAndActivoTrue(String email, String password);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRolAndActivoTrue(RolUsuario rol);
}
