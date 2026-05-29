package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_postulantes")
public class HistorialPostulante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "postulante_id", nullable = false)
    private Postulante postulante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private EstadoPostulante estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoPostulante estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(length = 700)
    private String observacion;

    @Column(name = "registrado_por")
    private String registradoPor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Postulante getPostulante() { return postulante; }
    public void setPostulante(Postulante postulante) { this.postulante = postulante; }
    public EstadoPostulante getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoPostulante estadoAnterior) { this.estadoAnterior = estadoAnterior; }
    public EstadoPostulante getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoPostulante estadoNuevo) { this.estadoNuevo = estadoNuevo; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String registradoPor) { this.registradoPor = registradoPor; }
}
