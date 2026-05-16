package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String enunciado;

    private String opcionCorrecta;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private OfertaLaboral oferta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getOpcionCorrecta() { return opcionCorrecta; }
    public void setOpcionCorrecta(String opcionCorrecta) { this.opcionCorrecta = opcionCorrecta; }
    public OfertaLaboral getOferta() { return oferta; }
    public void setOferta(OfertaLaboral oferta) { this.oferta = oferta; }
}
