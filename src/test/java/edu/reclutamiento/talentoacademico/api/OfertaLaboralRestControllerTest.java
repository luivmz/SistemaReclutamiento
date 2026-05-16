package edu.reclutamiento.talentoacademico.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.repository.OfertaRepository;
import edu.reclutamiento.talentoacademico.repository.PostulanteRepository;
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
 * Imports importantes para explicar en clase:
 * MockMvc ejecuta peticiones HTTP simuladas, jsonPath valida el JSON de respuesta
 * y los repositories preparan datos reales sobre H2 para probar el flujo refactorizado.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("TDD academico - OfertaLaboral REST")
class OfertaLaboralRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private PostulanteRepository postulanteRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        postulanteRepository.deleteAll();
        ofertaRepository.deleteAll();
    }

    private OfertaLaboral guardarOferta(String titulo, String descripcion, int vacantes) {
        OfertaLaboral oferta = new OfertaLaboral();
        oferta.setTitulo(titulo);
        oferta.setDescripcion(descripcion);
        oferta.setVacantes(vacantes);
        oferta.setActiva(true);
        return ofertaRepository.save(oferta);
    }

    @Test
    @DisplayName(" ROJO: Crear oferta laboral debe fallar si el titulo esta vacio")
    void rojo_crearOferta_debeFallarSiTituloEstaVacio() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String ofertaJson = """
            {
                "titulo": "",
                "descripcion": "Practicas profesionales",
                "vacantes": 2,
                "activa": true
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(post("/api/ofertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ofertaJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El titulo es obligatorio."));
    }

    @Test
    @DisplayName(" VERDE: Crear oferta laboral debe retornar la oferta con titulo valido")
    void verde_crearOferta_debeRetornarOfertaConTituloValido() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String ofertaJson = """
            {
                "titulo": "Practicante Java",
                "descripcion": "Apoyo en desarrollo backend",
                "vacantes": 2,
                "activa": true
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(post("/api/ofertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ofertaJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Practicante Java"))
                .andExpect(jsonPath("$.vacantes").value(2));
    }

    @Test
    @DisplayName(" REFACTOR: Crear oferta laboral debe persistir y retornar id")
    void refactor_crearOferta_debePersistirYRetornarId() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String ofertaJson = """
            {
                "titulo": "Analista QA",
                "descripcion": "Pruebas funcionales y automatizadas",
                "vacantes": 1,
                "activa": true
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(post("/api/ofertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ofertaJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Analista QA"));
    }

    @Test
    @DisplayName(" ROJO: Listar ofertas debe retornar lista vacia si no hay datos")
    void rojo_listarOfertas_debeRetornarListaVaciaSiNoHayDatos() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        // La base se limpia en @BeforeEach.

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(get("/api/ofertas"));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName(" VERDE: Listar ofertas debe retornar lista con ofertas")
    void verde_listarOfertas_debeRetornarListaConOfertas() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        guardarOferta("Practicante Backend", "Java y Spring Boot", 2);
        guardarOferta("Practicante QA", "Pruebas de software", 1);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(get("/api/ofertas"));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].titulo",
                        containsInAnyOrder("Practicante Backend", "Practicante QA")));
    }

    @Test
    @DisplayName(" REFACTOR: Buscar oferta por id debe retornar oferta correcta")
    void refactor_buscarOfertaPorId_debeRetornarOfertaCorrecta() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        OfertaLaboral oferta = guardarOferta("Asistente TI", "Soporte tecnico", 3);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(get("/api/ofertas/{id}", oferta.getId()));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(oferta.getId()))
                .andExpect(jsonPath("$.titulo").value("Asistente TI"));
    }

    @Test
    @DisplayName(" ROJO: Actualizar oferta debe fallar si no existe")
    void rojo_actualizarOferta_debeFallarSiNoExiste() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String cambiosJson = """
            {
                "titulo": "Oferta inexistente",
                "descripcion": "No debe actualizar",
                "vacantes": 1,
                "activa": true
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(put("/api/ofertas/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cambiosJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Oferta no encontrada con id 99999"));
    }

    @Test
    @DisplayName(" VERDE: Actualizar oferta laboral debe cambiar titulo")
    void verde_actualizarOferta_debeCambiarTitulo() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        OfertaLaboral oferta = guardarOferta("Titulo antiguo", "Descripcion inicial", 1);
        String cambiosJson = """
            {
                "titulo": "Titulo actualizado",
                "descripcion": "Descripcion inicial",
                "vacantes": 1,
                "activa": true
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(put("/api/ofertas/{id}", oferta.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cambiosJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo actualizado"));
    }

    @Test
    @DisplayName(" REFACTOR: Actualizar oferta debe mantener id y guardar cambios")
    void refactor_actualizarOferta_debeMantenerIdYGuardarCambios() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        OfertaLaboral oferta = guardarOferta("Dev Junior", "Backend", 1);
        String cambiosJson = """
            {
                "titulo": "Dev Junior Spring",
                "descripcion": "Backend con Spring Boot",
                "vacantes": 4,
                "activa": true
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(put("/api/ofertas/{id}", oferta.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cambiosJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(oferta.getId()))
                .andExpect(jsonPath("$.titulo").value("Dev Junior Spring"))
                .andExpect(jsonPath("$.vacantes").value(4));
    }

    @Test
    @DisplayName(" ROJO: Eliminar oferta debe fallar si no existe")
    void rojo_eliminarOferta_debeFallarSiNoExiste() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        // No se crea una oferta con el id 99999.

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(delete("/api/ofertas/{id}", 99999L));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Oferta no encontrada con id 99999"));
    }

    @Test
    @DisplayName(" VERDE: Eliminar oferta debe eliminar oferta existente")
    void verde_eliminarOferta_debeEliminarOfertaExistente() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        OfertaLaboral oferta = guardarOferta("Oferta temporal", "Se eliminara", 1);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(delete("/api/ofertas/{id}", oferta.getId()));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName(" REFACTOR: Eliminar oferta debe no aparecer en listado")
    void refactor_eliminarOferta_debeNoAparecerEnListado() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        OfertaLaboral oferta = guardarOferta("Oferta a retirar", "No debe listarse", 1);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        mockMvc.perform(delete("/api/ofertas/{id}", oferta.getId()))
                .andExpect(status().isNoContent());
        var resultado = mockMvc.perform(get("/api/ofertas"));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + oferta.getId() + ")]", hasSize(0)));
    }
}
