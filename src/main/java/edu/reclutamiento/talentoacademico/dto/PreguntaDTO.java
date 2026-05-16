package edu.reclutamiento.talentoacademico.dto;

public class PreguntaDTO {
    private Long id;
    private String enunciado;
    private String opcionCorrecta;
    private Long ofertaId;
    private String ofertaTitulo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getOpcionCorrecta() { return opcionCorrecta; }
    public void setOpcionCorrecta(String opcionCorrecta) { this.opcionCorrecta = opcionCorrecta; }
    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }
    public String getOfertaTitulo() { return ofertaTitulo; }
    public void setOfertaTitulo(String ofertaTitulo) { this.ofertaTitulo = ofertaTitulo; }
}
