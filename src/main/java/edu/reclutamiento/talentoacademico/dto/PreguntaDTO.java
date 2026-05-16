package edu.reclutamiento.talentoacademico.dto;

import java.util.ArrayList;
import java.util.List;

public class PreguntaDTO {
    private Long id;
    private String enunciado;
    private String opcionCorrecta;
    private List<Long> ofertaIds = new ArrayList<>();
    private String ofertasTitulos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getOpcionCorrecta() { return opcionCorrecta; }
    public void setOpcionCorrecta(String opcionCorrecta) { this.opcionCorrecta = opcionCorrecta; }
    public List<Long> getOfertaIds() { return ofertaIds; }
    public void setOfertaIds(List<Long> ofertaIds) { this.ofertaIds = ofertaIds; }
    public String getOfertasTitulos() { return ofertasTitulos; }
    public void setOfertasTitulos(String ofertasTitulos) { this.ofertasTitulos = ofertasTitulos; }
}
