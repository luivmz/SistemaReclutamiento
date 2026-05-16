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
    private TipoEntrevista tipoEntrevista;

    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String resultado;

    @Column(length = 700)
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "postulante_id")
    private Postulante postulante;

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
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Postulante getPostulante() { return postulante; }
    public void setPostulante(Postulante postulante) { this.postulante = postulante; }
}
