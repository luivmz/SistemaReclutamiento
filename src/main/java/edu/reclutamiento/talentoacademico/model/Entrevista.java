package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "entrevistas")
public class Entrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoEntrevista tipoEntrevista = TipoEntrevista.NORMAL;

    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String modalidad;

    @Column(length = 700)
    private String observacion;

    @Enumerated(EnumType.STRING)
    private EstadoEntrevista estadoEntrevista = EstadoEntrevista.PROGRAMADA;

    @ManyToOne
    @JoinColumn(name = "postulante_id")
    private Postulante postulante;

    @OneToOne(mappedBy = "entrevista")
    private ResultadoEntrevista resultadoEntrevista;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TipoEntrevista getTipoEntrevista() { return tipoEntrevista; }
    public void setTipoEntrevista(TipoEntrevista tipoEntrevista) { this.tipoEntrevista = tipoEntrevista; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public EstadoEntrevista getEstadoEntrevista() { return estadoEntrevista; }
    public void setEstadoEntrevista(EstadoEntrevista estadoEntrevista) { this.estadoEntrevista = estadoEntrevista; }
    public Postulante getPostulante() { return postulante; }
    public void setPostulante(Postulante postulante) { this.postulante = postulante; }
    public ResultadoEntrevista getResultadoEntrevista() { return resultadoEntrevista; }
    public void setResultadoEntrevista(ResultadoEntrevista resultadoEntrevista) { this.resultadoEntrevista = resultadoEntrevista; }
}
