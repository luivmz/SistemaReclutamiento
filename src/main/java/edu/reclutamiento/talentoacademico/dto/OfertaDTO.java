package edu.reclutamiento.talentoacademico.dto;

public class OfertaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer vacantes;
    private Long areaId;
    private String areaNombre;
    private Boolean activa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getVacantes() { return vacantes; }
    public void setVacantes(Integer vacantes) { this.vacantes = vacantes; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public String getAreaNombre() { return areaNombre; }
    public void setAreaNombre(String areaNombre) { this.areaNombre = areaNombre; }
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
