package edu.reclutamiento.talentoacademico.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.reclutamiento.talentoacademico.model.EstadoPostulante;
import edu.reclutamiento.talentoacademico.model.OfertaLaboral;
import edu.reclutamiento.talentoacademico.model.Postulante;
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
 * MockMvc prueba el REST Controller, MediaType envia JSON y los repositories
 * permiten preparar datos con JPA/H2 sin depender de datos globales.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("TDD academico - Postulante REST")
class PostulanteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private PostulanteRepository postulanteRepository;

    private OfertaLaboral ofertaBase;

    @BeforeEach
    void prepararBaseDeDatos() {
        postulanteRepository.deleteAll();
        ofertaRepository.deleteAll();

        ofertaBase = new OfertaLaboral();
        ofertaBase.setTitulo("Practicante de Sistemas");
        ofertaBase.setDescripcion("Oferta base para pruebas TDD");
        ofertaBase.setVacantes(5);
        ofertaBase.setActiva(true);
        ofertaBase = ofertaRepository.save(ofertaBase);
    }

    private Postulante guardarPostulante(String nombre, String telefono, EstadoPostulante estado) {
        Postulante postulante = new Postulante();
        postulante.setNombre(nombre);
        postulante.setEmail(nombre.toLowerCase().replace(" ", ".") + "@correo.edu");
        postulante.setTelefono(telefono);
        postulante.setEstado(estado);
        postulante.setAprobado(estado == EstadoPostulante.APROBADO);
        postulante.setPuntaje(0);
        postulante.setOferta(ofertaBase);
        return postulanteRepository.save(postulante);
    }

    @Test
    @DisplayName(" ROJO: Crear postulante debe fallar si nombre esta vacio")
    void rojo_crearPostulante_debeFallarSiNombreEstaVacio() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String postulanteJson = """
            {
                "nombre": "",
                "email": "sin.nombre@correo.edu",
                "telefono": "999111222",
                "ofertaId": %d
            }
            """.formatted(ofertaBase.getId());

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(post("/api/postulantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postulanteJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El nombre del postulante es obligatorio."));
    }

    @Test
    @DisplayName(" VERDE: Crear postulante debe retornar postulante con nombre")
    void verde_crearPostulante_debeRetornarPostulanteConNombre() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String postulanteJson = """
            {
                "nombre": "Ana Lopez",
                "email": "ana.lopez@correo.edu",
                "telefono": "999111222",
                "ofertaId": %d
            }
            """.formatted(ofertaBase.getId());

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(post("/api/postulantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postulanteJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Ana Lopez"));
    }

    @Test
    @DisplayName(" REFACTOR: Crear postulante debe persistir con estado POSTULADO")
    void refactor_crearPostulante_debePersistirConEstadoPostulado() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String postulanteJson = """
            {
                "nombre": "Maria Salas",
                "email": "maria.salas@correo.edu",
                "telefono": "999333444",
                "estado": "APROBADO",
                "ofertaId": %d
            }
            """.formatted(ofertaBase.getId());

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(post("/api/postulantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postulanteJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("POSTULADO"))
                .andExpect(jsonPath("$.ofertaId").value(ofertaBase.getId()));
    }

    @Test
    @DisplayName(" ROJO: Listar postulantes debe retornar lista vacia si no hay datos")
    void rojo_listarPostulantes_debeRetornarListaVaciaSiNoHayDatos() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        // Solo existe una oferta base; no hay postulantes registrados.

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(get("/api/postulantes"));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName(" VERDE: Listar postulantes debe retornar lista con postulantes")
    void verde_listarPostulantes_debeRetornarListaConPostulantes() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        guardarPostulante("Pedro Ruiz", "999000111", EstadoPostulante.POSTULADO);
        guardarPostulante("Lucia Diaz", "999000222", EstadoPostulante.POSTULADO);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(get("/api/postulantes"));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nombre",
                        containsInAnyOrder("Pedro Ruiz", "Lucia Diaz")));
    }

    @Test
    @DisplayName(" REFACTOR: Buscar postulante por id debe retornar postulante correcto")
    void refactor_buscarPostulantePorId_debeRetornarPostulanteCorrecto() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        Postulante postulante = guardarPostulante("Carlos Vega", "999000333", EstadoPostulante.POSTULADO);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(get("/api/postulantes/{id}", postulante.getId()));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postulante.getId()))
                .andExpect(jsonPath("$.nombre").value("Carlos Vega"))
                .andExpect(jsonPath("$.estado").value("POSTULADO"));
    }

    @Test
    @DisplayName(" ROJO: Actualizar postulante debe fallar si no existe")
    void rojo_actualizarPostulante_debeFallarSiNoExiste() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        String cambiosJson = """
            {
                "telefono": "999999999"
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(put("/api/postulantes/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cambiosJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Postulante no encontrado con id 99999"));
    }

    @Test
    @DisplayName(" VERDE: Actualizar postulante debe cambiar telefono")
    void verde_actualizarPostulante_debeCambiarTelefono() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        Postulante postulante = guardarPostulante("Jose Sanchez", "999000444", EstadoPostulante.POSTULADO);
        String cambiosJson = """
            {
                "telefono": "988777666"
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(put("/api/postulantes/{id}", postulante.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cambiosJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postulante.getId()))
                .andExpect(jsonPath("$.telefono").value("988777666"));
    }

    @Test
    @DisplayName(" REFACTOR: Actualizar postulante debe actualizar estado")
    void refactor_actualizarPostulante_debeActualizarEstado() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        Postulante postulante = guardarPostulante("Sara Rojas", "999000555", EstadoPostulante.POSTULADO);
        String cambiosJson = """
            {
                "estado": "APROBADO"
            }
            """;

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(put("/api/postulantes/{id}", postulante.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cambiosJson));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADO"))
                .andExpect(jsonPath("$.aprobado").value(true));
    }

    @Test
    @DisplayName(" ROJO: Eliminar postulante debe fallar si no existe")
    void rojo_eliminarPostulante_debeFallarSiNoExiste() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        // No se crea un postulante con el id 99999.

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(delete("/api/postulantes/{id}", 99999L));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Postulante no encontrado con id 99999"));
    }

    @Test
    @DisplayName(" VERDE: Eliminar postulante debe eliminar postulante existente")
    void verde_eliminarPostulante_debeEliminarPostulanteExistente() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        Postulante postulante = guardarPostulante("Luis Mora", "999000666", EstadoPostulante.POSTULADO);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        var resultado = mockMvc.perform(delete("/api/postulantes/{id}", postulante.getId()));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName(" REFACTOR: Eliminar postulante debe no aparecer en activos")
    void refactor_eliminarPostulante_debeNoAparecerEnActivos() throws Exception {
        // ==========================================================
        // PARTE 1: PREPARAR (Given)
        // ==========================================================
        Postulante postulante = guardarPostulante("Diego Paz", "999000777", EstadoPostulante.POSTULADO);

        // ==========================================================
        // PARTE 2: EJECUTAR (When)
        // ==========================================================
        mockMvc.perform(delete("/api/postulantes/{id}", postulante.getId()))
                .andExpect(status().isNoContent());
        var resultado = mockMvc.perform(get("/api/postulantes/activos"));

        // ==========================================================
        // PARTE 3: VERIFICAR (Then)
        // ==========================================================
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + postulante.getId() + ")]", hasSize(0)));
    }
}
