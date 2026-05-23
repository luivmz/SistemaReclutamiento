package edu.reclutamiento.talentoacademico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_entrevista")
public class ResultadoEntrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "entrevista_id", unique = true, nullable = false)
    private Entrevista entrevista;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoResultado resultado = EstadoResultado.PENDIENTE;

    private Integer puntaje;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(columnDefinition = "TEXT")
    private String recomendacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "registrado_por", length = 120)
    private String registradoPor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Entrevista getEntrevista() { return entrevista; }
    public void setEntrevista(Entrevista entrevista) { this.entrevista = entrevista; }
    public EstadoResultado getResultado() { return resultado; }
    public void setResultado(EstadoResultado resultado) { this.resultado = resultado; }
    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getRecomendacion() { return recomendacion; }
    public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String registradoPor) { this.registradoPor = registradoPor; }
}
