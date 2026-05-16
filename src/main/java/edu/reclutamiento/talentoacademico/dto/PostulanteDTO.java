package edu.reclutamiento.talentoacademico.dto;

public class PostulanteDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String cv;
    private String estado;
    private Long ofertaId;
    private String ofertaTitulo;
    private Integer puntaje;
    private Boolean aprobado;

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
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }
    public String getOfertaTitulo() { return ofertaTitulo; }
    public void setOfertaTitulo(String ofertaTitulo) { this.ofertaTitulo = ofertaTitulo; }
    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
    public Boolean getAprobado() { return aprobado; }
    public void setAprobado(Boolean aprobado) { this.aprobado = aprobado; }
}
