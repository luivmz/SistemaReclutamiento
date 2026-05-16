package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ofertas_laborales")
public class OfertaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(length = 800)
    private String descripcion;

    private Integer vacantes;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    private Boolean activa = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getVacantes() { return vacantes; }
    public void setVacantes(Integer vacantes) { this.vacantes = vacantes; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
