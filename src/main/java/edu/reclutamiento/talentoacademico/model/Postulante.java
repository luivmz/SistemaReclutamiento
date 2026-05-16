package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "postulantes")
public class Postulante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String telefono;

    @Column(length = 700)
    private String experiencia;

    @Column(length = 700)
    private String habilidades;

    private String cv;
    private LocalDate fechaPostulacion = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private EstadoPostulante estado = EstadoPostulante.POSTULADO;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private OfertaLaboral oferta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private Integer puntaje = 0;
    private Boolean aprobado = false;

    @Column(length = 700)
    private String observacion;

    public boolean estaEnHistorial() {
        return estado == EstadoPostulante.APROBADO
                || estado == EstadoPostulante.RECHAZADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }
    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    public LocalDate getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(LocalDate fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }
    public EstadoPostulante getEstado() { return estado; }
    public void setEstado(EstadoPostulante estado) { this.estado = estado; }
    public OfertaLaboral getOferta() { return oferta; }
    public void setOferta(OfertaLaboral oferta) { this.oferta = oferta; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
    public Boolean getAprobado() { return aprobado; }
    public void setAprobado(Boolean aprobado) { this.aprobado = aprobado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
