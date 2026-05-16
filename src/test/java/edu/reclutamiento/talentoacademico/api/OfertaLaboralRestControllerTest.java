package edu.reclutamiento.talentoacademico.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.reclutamiento.talentoacademico.dto.OfertaDTO;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Pruebas TDD para el CRUD REST de {@link OfertaLaboral}.
 *
 * Sigue el ciclo:
 *   ROJO       -> prueba que verifica un escenario de error / validacion (falla antes de implementar).
 *   VERDE      -> prueba que verifica el camino feliz (pasa con la implementacion minima).
 *   REFACTOR   -> prueba que verifica detalles de calidad sin cambiar el comportamiento.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("TDD - OfertaLaboral REST Controller")
class OfertaLaboralRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private PostulanteRepository postulanteRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        // Garantizamos un estado controlado en cada prueba.
        postulanteRepository.deleteAll();
        ofertaRepository.deleteAll();
    }

    private OfertaLaboral guardarOfertaDePrueba(String titulo, int vacantes) {
        OfertaLaboral oferta = new OfertaLaboral();
        oferta.setTitulo(titulo);
        oferta.setDescripcion("Descripcion academica de " + titulo);
        oferta.setVacantes(vacantes);
        oferta.setActiva(true);
        return ofertaRepository.save(oferta);
    }

    // ============================================================
    //                          CREATE
    // ============================================================

    @Test
    @DisplayName("1) ROJO - crearOferta debe fallar si los datos son invalidos")
    void rojo_crearOferta_debeFallarSiDatosInvalidos() {
        /*
         * FASE ROJA del ciclo TDD.
         * Comportamiento esperado: POST /api/ofertas con titulo vacio y
         * vacantes = 0 debe responder HTTP 400 Bad Request.
         * Esta prueba se declara como PENDIENTE para demostrar que en TDD
         * primero se escribe la prueba que falla y luego se implementa.
         */
        Assertions.fail("Fase ROJA: pendiente implementar validacion en POST /api/ofertas (titulo y vacantes).");
    }

    @Test
    @DisplayName("2) VERDE - crearOferta debe crear correctamente con datos validos")
    void verde_crearOferta_debeCrearConDatosValidos() throws Exception {
        OfertaDTO nueva = new OfertaDTO();
        nueva.setTitulo("Practicante Java");
        nueva.setDescripcion("Apoyo en desarrollo backend.");
        nueva.setVacantes(2);
        nueva.setActiva(true);

        mockMvc.perform(post("/api/ofertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo", is("Practicante Java")));
    }

    @Test
    @DisplayName("3) REFACTOR - crearOferta debe responder un DTO claro con todos los campos")
    void refactor_crearOferta_debeResponderDTOClaro() throws Exception {
        OfertaDTO nueva = new OfertaDTO();
        nueva.setTitulo("Asistente Administrativo");
        nueva.setDescripcion("Apoyo administrativo.");
        nueva.setVacantes(3);
        nueva.setActiva(true);

        mockMvc.perform(post("/api/ofertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo", is("Asistente Administrativo")))
                .andExpect(jsonPath("$.descripcion", is("Apoyo administrativo.")))
                .andExpect(jsonPath("$.vacantes", is(3)))
                .andExpect(jsonPath("$.activa", is(true)));
    }

    // ============================================================
    //                            READ
    // ============================================================

    @Test
    @DisplayName("4) ROJO - listarOfertas debe retornar vacio cuando no hay datos")
    void rojo_listarOfertas_debeRetornarVacioSiNoHayDatos() {
        /*
         * FASE ROJA del ciclo TDD.
         * Comportamiento esperado: GET /api/ofertas debe responder 200 OK
         * con un arreglo vacio cuando no existen ofertas registradas.
         * En la fase VERDE se conectara MockMvc al endpoint para validarlo.
         */
        Assertions.fail("Fase ROJA: pendiente implementar listado vacio en GET /api/ofertas.");
    }

    @Test
    @DisplayName("5) VERDE - listarOfertas debe retornar las ofertas existentes")
    void verde_listarOfertas_debeRetornarOfertasExistentes() throws Exception {
        guardarOfertaDePrueba("Practicante QA", 1);
        guardarOfertaDePrueba("Practicante Backend", 2);

        mockMvc.perform(get("/api/ofertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].titulo",
                        containsInAnyOrder("Practicante QA", "Practicante Backend")));
    }

    @Test
    @DisplayName("6) REFACTOR - obtenerOfertaPorId debe retornar un detalle claro")
    void refactor_obtenerOfertaPorId_debeRetornarDetalleClaro() throws Exception {
        OfertaLaboral guardada = guardarOfertaDePrueba("Practicante Front-end", 4);

        mockMvc.perform(get("/api/ofertas/{id}", guardada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(guardada.getId().intValue())))
                .andExpect(jsonPath("$.titulo", is("Practicante Front-end")))
                .andExpect(jsonPath("$.vacantes", is(4)))
                .andExpect(jsonPath("$.activa", is(true)));
    }

    // ============================================================
    //                           UPDATE
    // ============================================================

    @Test
    @DisplayName("7) ROJO - actualizarOferta debe fallar si la oferta no existe")
    void rojo_actualizarOferta_debeFallarSiNoExiste() {
        /*
         * FASE ROJA del ciclo TDD.
         * Comportamiento esperado: PUT /api/ofertas/{id} con un id inexistente
         * debe responder HTTP 404 Not Found.
         * Se declara pendiente para reflejar el primer paso del ciclo TDD.
         */
        Assertions.fail("Fase ROJA: pendiente implementar PUT /api/ofertas/{id} con manejo de 404.");
    }

    @Test
    @DisplayName("8) VERDE - actualizarOferta debe modificar los datos existentes")
    void verde_actualizarOferta_debeModificarDatos() throws Exception {
        OfertaLaboral original = guardarOfertaDePrueba("Original", 1);

        OfertaDTO cambios = new OfertaDTO();
        cambios.setTitulo("Modificada");
        cambios.setDescripcion("Descripcion actualizada");
        cambios.setVacantes(5);
        cambios.setActiva(true);

        mockMvc.perform(put("/api/ofertas/{id}", original.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Modificada")))
                .andExpect(jsonPath("$.vacantes", is(5)));
    }

    @Test
    @DisplayName("9) REFACTOR - actualizarOferta debe mantener el id original")
    void refactor_actualizarOferta_debeMantenerIdOriginal() throws Exception {
        OfertaLaboral original = guardarOfertaDePrueba("Original", 1);
        Long idOriginal = original.getId();

        OfertaDTO cambios = new OfertaDTO();
        cambios.setTitulo("Renombrada");
        cambios.setVacantes(2);
        cambios.setActiva(true);

        mockMvc.perform(put("/api/ofertas/{id}", idOriginal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(idOriginal.intValue())))
                .andExpect(jsonPath("$.titulo", is("Renombrada")));
    }

    // ============================================================
    //                           DELETE
    // ============================================================

    @Test
    @DisplayName("10) ROJO - eliminarOferta debe fallar si la oferta no existe")
    void rojo_eliminarOferta_debeFallarSiNoExiste() {
        /*
         * FASE ROJA del ciclo TDD.
         * Comportamiento esperado: DELETE /api/ofertas/{id} con un id inexistente
         * debe responder HTTP 404 Not Found.
         * Pendiente de implementar en fase VERDE.
         */
        Assertions.fail("Fase ROJA: pendiente implementar DELETE /api/ofertas/{id} con manejo de 404.");
    }

    @Test
    @DisplayName("11) VERDE - eliminarOferta debe eliminar una oferta existente")
    void verde_eliminarOferta_debeEliminarExistente() throws Exception {
        OfertaLaboral oferta = guardarOfertaDePrueba("Para eliminar", 1);

        mockMvc.perform(delete("/api/ofertas/{id}", oferta.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("12) REFACTOR - oferta eliminada no debe aparecer en el listado")
    void refactor_eliminarOferta_debeNoAparecerEnListado() throws Exception {
        OfertaLaboral oferta = guardarOfertaDePrueba("Desaparecera", 1);

        mockMvc.perform(delete("/api/ofertas/{id}", oferta.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/ofertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + oferta.getId() + ")]", hasSize(0)));
    }
}
