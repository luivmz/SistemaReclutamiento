package edu.reclutamiento.talentoacademico.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class EntrevistaDTO {
    private Long id;
    private String tipoEntrevista;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String resultado;
    private String observacion;
    private Long postulanteId;
    private String postulanteNombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipoEntrevista() { return tipoEntrevista; }
    public void setTipoEntrevista(String tipoEntrevista) { this.tipoEntrevista = tipoEntrevista; }
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
    public Long getPostulanteId() { return postulanteId; }
    public void setPostulanteId(Long postulanteId) { this.postulanteId = postulanteId; }
    public String getPostulanteNombre() { return postulanteNombre; }
    public void setPostulanteNombre(String postulanteNombre) { this.postulanteNombre = postulanteNombre; }
}
