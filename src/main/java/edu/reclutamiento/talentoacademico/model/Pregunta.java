package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String enunciado;

    private String opcionCorrecta;

    @ManyToMany
    @JoinTable(
            name = "preguntas_ofertas",
            joinColumns = @JoinColumn(name = "pregunta_id"),
            inverseJoinColumns = @JoinColumn(name = "oferta_id")
    )
    private Set<OfertaLaboral> ofertas = new LinkedHashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getOpcionCorrecta() { return opcionCorrecta; }
    public void setOpcionCorrecta(String opcionCorrecta) { this.opcionCorrecta = opcionCorrecta; }
    public Set<OfertaLaboral> getOfertas() { return ofertas; }
    public void setOfertas(Set<OfertaLaboral> ofertas) { this.ofertas = ofertas; }
}
