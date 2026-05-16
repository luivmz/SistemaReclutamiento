package edu.reclutamiento.talentoacademico.service.impl;

import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
import edu.reclutamiento.talentoacademico.mapper.PostulanteMapper;
import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.model.Postulante;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import edu.reclutamiento.talentoacademico.service.PostulanteService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PostulanteServiceImpl implements PostulanteService {
    private final PostulanteRepository postulanteRepository;
    private final OfertaRepository ofertaRepository;

    public PostulanteServiceImpl(PostulanteRepository postulanteRepository, OfertaRepository ofertaRepository) {
        this.postulanteRepository = postulanteRepository;
        this.ofertaRepository = ofertaRepository;
    }

    public List<PostulanteDTO> listar() {
        return postulanteRepository.findAll().stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarActivos() {
        return postulanteRepository.findByEstado(EstadoPostulante.ACTIVO).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarHistorial() {
        return postulanteRepository.findByEstadoIn(List.of(
                EstadoPostulante.APROBADO,
                EstadoPostulante.DESAPROBADO,
                EstadoPostulante.FINALIZADO
        )).stream().map(PostulanteMapper::toDTO).toList();
    }

    public List<PostulanteDTO> listarPorEmail(String email) {
        return postulanteRepository.findByEmail(email).stream().map(PostulanteMapper::toDTO).toList();
    }

    public PostulanteDTO postular(PostulanteDTO dto) {
        Postulante postulante = PostulanteMapper.toEntity(dto);
        postulante.setEstado(EstadoPostulante.ACTIVO);
        postulante.setAprobado(false);
        asignarOferta(dto, postulante);
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public PostulanteDTO actualizar(PostulanteDTO dto) {
        Postulante postulante = PostulanteMapper.toEntity(dto);
        asignarOferta(dto, postulante);
        return PostulanteMapper.toDTO(postulanteRepository.save(postulante));
    }

    public void aprobar(Long id) {
        cambiarEstado(id, EstadoPostulante.APROBADO, true);
    }

    public void rechazar(Long id) {
        cambiarEstado(id, EstadoPostulante.DESAPROBADO, false);
    }

    public void finalizar(Long id) {
        cambiarEstado(id, EstadoPostulante.FINALIZADO, false);
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
}
