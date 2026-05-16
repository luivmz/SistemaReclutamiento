package edu.reclutamiento.talentoacademico.config;

import edu.reclutamiento.talentoacademico.model.Area;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.model.RolUsuario;
import edu.reclutamiento.talentoacademico.model.Usuario;
import edu.reclutamiento.talentoacademico.repository.AreaRepository;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final AreaRepository areaRepository;
    private final OfertaRepository ofertaRepository;

    public DataInitializer(UsuarioRepository usuarioRepository, AreaRepository areaRepository, OfertaRepository ofertaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.areaRepository = areaRepository;
        this.ofertaRepository = ofertaRepository;
    }

    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombre("Administrador Academico");
        admin.setEmail("admin@talento.edu");
        admin.setPassword("123456");
        admin.setRol(RolUsuario.ADMIN);
        admin.setActivo(true);
        usuarioRepository.save(admin);

        Usuario postulante = new Usuario();
        postulante.setNombre("Postulante Demo");
        postulante.setEmail("postulante@talento.edu");
        postulante.setPassword("123456");
        postulante.setRol(RolUsuario.POSTULANTE);
        postulante.setActivo(true);
        usuarioRepository.save(postulante);

        Area tecnologia = new Area();
        tecnologia.setNombre("Tecnologia");
        tecnologia.setDescripcion("Area de desarrollo y soporte de sistemas.");
        areaRepository.save(tecnologia);

        Area administracion = new Area();
        administracion.setNombre("Administracion");
        administracion.setDescripcion("Area de gestion documentaria y apoyo administrativo.");
        areaRepository.save(administracion);

        OfertaLaboral oferta = new OfertaLaboral();
        oferta.setTitulo("Practicante de Desarrollo Java");
        oferta.setDescripcion("Apoyo en mantenimiento de aplicaciones web con Spring Boot.");
        oferta.setVacantes(2);
        oferta.setArea(tecnologia);
        oferta.setActiva(true);
        ofertaRepository.save(oferta);
    }
}
