package edu.reclutamiento.talentoacademico.dto;

public class PostulanteDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String experiencia;
    private String habilidades;
    private String cv;
    private String fechaPostulacion;
    private String estado;
    private Long ofertaId;
    private String ofertaTitulo;
    private String areaNombre;
    private Long usuarioId;
    private Boolean aprobado;
    private String observacion;

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
    public String getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(String fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }
    public String getOfertaTitulo() { return ofertaTitulo; }
    public void setOfertaTitulo(String ofertaTitulo) { this.ofertaTitulo = ofertaTitulo; }
    public String getAreaNombre() { return areaNombre; }
    public void setAreaNombre(String areaNombre) { this.areaNombre = areaNombre; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Boolean getAprobado() { return aprobado; }
    public void setAprobado(Boolean aprobado) { this.aprobado = aprobado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
