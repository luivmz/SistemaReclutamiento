package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.mapper.PostulanteMapper;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.model.Usuario;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.repository.UsuarioRepository;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class PostulanteServiceImpl implements PostulanteService {
    private final PostulanteRepository postulanteRepository;
    private final OfertaRepository ofertaRepository;
    private final UsuarioRepository usuarioRepository;

    public PostulanteServiceImpl(PostulanteRepository postulanteRepository, OfertaRepository ofertaRepository,
                                 UsuarioRepository usuarioRepository) {
        this.postulanteRepository = postulanteRepository;
        this.ofertaRepository = ofertaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PostulanteDTO> listar() {
        return postulanteRepository.findAll().stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarActivos() {
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.POSTULADO,
                EstadoPostulante.EN_EVALUACION
        )).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarHistorial() {
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.APROBADO,
                EstadoPostulante.RECHAZADO
        )).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarPorEmail(String email) {
        return postulanteRepository.findByEmail(email).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarPorUsuario(Long usuarioId) {
        return postulanteRepository.findByUsuarioId(usuarioId).stream().map(PostulanteMapper::toDTO).toList();
    }

    public PostulanteDTO buscar(Long id) {
        return postulanteRepository.findById(id).map(PostulanteMapper::toDTO).orElse(null);
    }

    public PostulanteDTO postular(PostulanteDTO dto, Long usuarioId) {
        if (dto.getOfertaId() != null && yaPostulo(usuarioId, dto.getOfertaId())) {
            throw new IllegalStateException("Ya postulaste a esta oferta.");
        }
        dto.setId(null);
        Postulante postulante = PostulanteMapper.toEntity(dto);
        postulante.setEstado(EstadoPostulante.POSTULADO);
        postulante.setAprobado(false);
        asignarOferta(dto, postulante);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        postulante.setUsuario(usuario);
        postulante.setNombre(usuario.getNombre());
        postulante.setEmail(usuario.getEmail());
        if (postulante.getTelefono() == null || postulante.getTelefono().isBlank()) {
            postulante.setTelefono(usuario.getTelefono());
        }
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public PostulanteDTO crear(PostulanteDTO dto) {
        // F1 - ROJO:
        // La prueba inicia exigiendo que un postulante sin nombre sea rechazado con HTTP 400.
        //
        // F2 - VERDE:
        // La implementacion minima podria devolver un DTO con el nombre recibido.
        //
        // F3 - REFACTORIZAR:
        // El codigo final valida, busca la oferta, usa mapper, persiste con repository
        // y fuerza el estado inicial POSTULADO.
        validarCreacion(dto);
        OfertaLaboral oferta = ofertaRepository.findById(dto.getOfertaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La oferta indicada (" + dto.getOfertaId() + ") no existe."));
        dto.setId(null);
        Postulante postulante = PostulanteMapper.toEntity(dto);
        postulante.setEstado(EstadoPostulante.POSTULADO);
        postulante.setAprobado(false);
        postulante.setOferta(oferta);
        if (dto.getUsuarioId() != null) {
            usuarioRepository.findById(dto.getUsuarioId()).ifPresent(postulante::setUsuario);
        }
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public PostulanteDTO actualizar(PostulanteDTO dto) {
        Postulante postulante = postulanteRepository.findById(dto.getId()).orElseThrow();
        postulante.setNombre(dto.getNombre());
        postulante.setEmail(dto.getEmail());
        postulante.setTelefono(dto.getTelefono());
        postulante.setExperiencia(dto.getExperiencia());
        postulante.setHabilidades(dto.getHabilidades());
        postulante.setCv(dto.getCv());
        postulante.setObservacion(dto.getObservacion());
        if (dto.getPuntaje() != null) {
            postulante.setPuntaje(dto.getPuntaje());
        }
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            postulante.setEstado(EstadoPostulante.valueOf(dto.getEstado()));
        }
        asignarOferta(dto, postulante);
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public PostulanteDTO actualizarPostulante(Long id, PostulanteDTO dto) {
        // F1 - ROJO:
        // La prueba exige 404 cuando se actualiza un postulante inexistente.
        //
        // F2 - VERDE:
        // El cambio minimo actualiza el telefono de un postulante existente.
        //
        // F3 - REFACTORIZAR:
        // Se actualizan solo campos enviados y el estado mantiene sincronizado aprobado.
        Postulante postulante = postulanteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Postulante no encontrado con id " + id));
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            postulante.setNombre(dto.getNombre());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            postulante.setEmail(dto.getEmail());
        }
        if (dto.getTelefono() != null) {
            postulante.setTelefono(dto.getTelefono());
        }
        if (dto.getExperiencia() != null) {
            postulante.setExperiencia(dto.getExperiencia());
        }
        if (dto.getHabilidades() != null) {
            postulante.setHabilidades(dto.getHabilidades());
        }
        if (dto.getCv() != null) {
            postulante.setCv(dto.getCv());
        }
        if (dto.getObservacion() != null) {
            postulante.setObservacion(dto.getObservacion());
        }
        if (dto.getPuntaje() != null) {
            postulante.setPuntaje(dto.getPuntaje());
        }
        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            EstadoPostulante nuevoEstado = EstadoPostulante.valueOf(dto.getEstado());
            postulante.setEstado(nuevoEstado);
            postulante.setAprobado(nuevoEstado == EstadoPostulante.APROBADO);
        }
        if (dto.getOfertaId() != null) {
            ofertaRepository.findById(dto.getOfertaId()).ifPresent(postulante::setOferta);
        }
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public void eliminarReal(Long id) {
        // F1 - ROJO:
        // La prueba exige 404 al eliminar un postulante que no existe.
        //
        // F2 - VERDE:
        // El cambio minimo elimina un postulante existente.
        //
        // F3 - REFACTORIZAR:
        // Al borrar con repository, el postulante deja de aparecer en /api/postulantes/activos.
        if (!postulanteRepository.existsById(id)) {
            throw new NoSuchElementException("Postulante no encontrado con id " + id);
        }
        postulanteRepository.deleteById(id);
    }

    public void aprobar(Long id) {
        cambiarEstado(id, EstadoPostulante.APROBADO, true);
    }

    public void rechazar(Long id) {
        cambiarEstado(id, EstadoPostulante.RECHAZADO, false);
    }

    public void finalizar(Long id) {
        cambiarEstado(id, EstadoPostulante.RECHAZADO, false);
    }

    public boolean yaPostulo(Long usuarioId, Long ofertaId) {
        return postulanteRepository.existsByUsuarioIdAndOfertaId(usuarioId, ofertaId);
    }

    public long contarPorUsuario(Long usuarioId) {
        return postulanteRepository.countByUsuarioId(usuarioId);
    }

    public long contarPorUsuarioYEstado(Long usuarioId, String estado) {
        return postulanteRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPostulante.valueOf(estado));
    }

    public void marcarEnEvaluacion(Long id) {
        cambiarEstado(id, EstadoPostulante.EN_EVALUACION, false);
    }

    public void registrarEvaluacion(Long id, int puntaje, boolean aprobado) {
        Postulante postulante = postulanteRepository.findById(id).orElseThrow();
        postulante.setPuntaje(puntaje);
        postulante.setAprobado(aprobado);
        postulante.setEstado(aprobado ? EstadoPostulante.APROBADO : EstadoPostulante.RECHAZADO);
        postulante.setObservacion(aprobado ? "Evaluacion aprobada." : "Evaluacion desaprobada.");
        postulanteRepository.save(postulante);
    }

    public void cambiarEstado(Long id, String estado) {
        EstadoPostulante nuevoEstado = EstadoPostulante.valueOf(estado);
        Postulante postulante = postulanteRepository.findById(id).orElseThrow();
        postulante.setEstado(nuevoEstado);
        postulante.setAprobado(nuevoEstado == EstadoPostulante.APROBADO);
        postulanteRepository.save(postulante);
    }

    private void cambiarEstado(Long id, EstadoPostulante estado, boolean aprobado) {
        Postulante postulante = postulanteRepository.findById(id).orElseThrow();
        postulante.setEstado(estado);
        postulante.setAprobado(aprobado);
        postulanteRepository.save(postulante);
    }

    private void asignarOferta(PostulanteDTO dto, Postulante postulante) {
        if (dto.getOfertaId() != null) {
            OfertaLaboral oferta = ofertaRepository.findById(dto.getOfertaId()).orElse(null);
            postulante.setOferta(oferta);
        }
    }

    private void validarCreacion(PostulanteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El postulante no puede ser nulo.");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del postulante es obligatorio.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email del postulante es obligatorio.");
        }
        if (dto.getOfertaId() == null) {
            throw new IllegalArgumentException("La oferta del postulante es obligatoria.");
        }
    }
}
