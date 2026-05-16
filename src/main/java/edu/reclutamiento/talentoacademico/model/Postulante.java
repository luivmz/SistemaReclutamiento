package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "postulantes")
public class Postulante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String telefono;
    private String cv;

    @Enumerated(EnumType.STRING)
    private EstadoPostulante estado = EstadoPostulante.ACTIVO;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private OfertaLaboral oferta;

    private Integer puntaje = 0;
    private Boolean aprobado = false;

    public boolean estaEnHistorial() {
        return estado == EstadoPostulante.APROBADO
                || estado == EstadoPostulante.DESAPROBADO
                || estado == EstadoPostulante.FINALIZADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    public EstadoPostulante getEstado() { return estado; }
    public void setEstado(EstadoPostulante estado) { this.estado = estado; }
    public OfertaLaboral getOferta() { return oferta; }
    public void setOferta(OfertaLaboral oferta) { this.oferta = oferta; }
    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
    public Boolean getAprobado() { return aprobado; }
    public void setAprobado(Boolean aprobado) { this.aprobado = aprobado; }
}
