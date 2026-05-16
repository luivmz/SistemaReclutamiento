package edu.reclutamiento.talentoacademico.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.reclutamiento.talentoacademico.dto.PostulanteDTO;
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
 * Pruebas TDD para el CRUD REST de Postulante.
 *
 * Las pruebas estan ordenadas siguiendo el ciclo academico ROJO -> VERDE -> REFACTOR
 * para cada operacion del CRUD: Create, Read, Update, Delete.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("TDD - Postulante REST Controller")
class PostulanteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private PostulanteRepository postulanteRepository;

    private OfertaLaboral ofertaBase;

    @BeforeEach
    void prepararEntorno() {
        // Limpiamos primero los postulantes (FK) y luego las ofertas.
        postulanteRepository.deleteAll();
        ofertaRepository.deleteAll();

        // Creamos una oferta base reutilizable para los escenarios que la requieran.
        ofertaBase = new OfertaLaboral();
        ofertaBase.setTitulo("Practicante de Pruebas");
        ofertaBase.setDescripcion("Oferta usada por las pruebas TDD.");
        ofertaBase.setVacantes(3);
        ofertaBase.setActiva(true);
        ofertaBase = ofertaRepository.save(ofertaBase);
    }

    private Postulante guardarPostulanteDePrueba(String nombre, EstadoPostulante estado) {
        Postulante postulante = new Postulante();
        postulante.setNombre(nombre);
        postulante.setEmail(nombre.toLowerCase().replace(' ', '.') + "@talento.edu");
        postulante.setTelefono("999000000");
        postulante.setEstado(estado);
        postulante.setAprobado(estado == EstadoPostulante.APROBADO);
        postulante.setPuntaje(0);
        postulante.setOferta(ofertaBase);
        return postulanteRepository.save(postulante);
    }

    private PostulanteDTO dtoValido(String nombre) {
        PostulanteDTO dto = new PostulanteDTO();
        dto.setNombre(nombre);
        dto.setEmail(nombre.toLowerCase().replace(' ', '.') + "@talento.edu");
        dto.setTelefono("999111222");
        dto.setOfertaId(ofertaBase.getId());
        return dto;
    }

    // ============================================================
    //                          CREATE
    // ============================================================

    @Test
    @DisplayName("13) ROJO - crearPostulante debe fallar si la oferta no existe")
    void rojo_crearPostulante_debeFallarSiOfertaNoExiste() throws Exception {
        PostulanteDTO dto = dtoValido("Postulante Demo");
        dto.setOfertaId(9999L); // Oferta inexistente.

        mockMvc.perform(post("/api/postulantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("14) VERDE - crearPostulante debe registrarse correctamente en la oferta")
    void verde_crearPostulante_debeRegistrarseEnOferta() throws Exception {
        PostulanteDTO dto = dtoValido("Ana Lopez");

        mockMvc.perform(post("/api/postulantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Ana Lopez")))
                .andExpect(jsonPath("$.ofertaId", is(ofertaBase.getId().intValue())));
    }

    @Test
    @DisplayName("15) REFACTOR - crearPostulante debe iniciar siempre con estado POSTULADO")
    void refactor_crearPostulante_debeIniciarConEstadoPostulado() throws Exception {
        PostulanteDTO dto = dtoValido("Maria Salas");
        // Aunque el cliente envie otro estado, el servicio debe forzar POSTULADO.
        dto.setEstado("APROBADO");

        mockMvc.perform(post("/api/postulantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("POSTULADO")));
    }

    // ============================================================
    //                            READ
    // ============================================================

    @Test
    @DisplayName("16) ROJO - listarPostulantes debe retornar vacio cuando no hay datos")
    void rojo_listarPostulantes_debeRetornarVacioSiNoHayDatos() throws Exception {
        // Nos aseguramos de no tener postulantes registrados.
        postulanteRepository.deleteAll();

        mockMvc.perform(get("/api/postulantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("17) VERDE - listarPostulantes debe retornar los postulantes existentes")
    void verde_listarPostulantes_debeRetornarPostulantesExistentes() throws Exception {
        guardarPostulanteDePrueba("Pedro Ruiz", EstadoPostulante.POSTULADO);
        guardarPostulanteDePrueba("Lucia Diaz", EstadoPostulante.POSTULADO);

        mockMvc.perform(get("/api/postulantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nombre",
                        containsInAnyOrder("Pedro Ruiz", "Lucia Diaz")));
    }

    @Test
    @DisplayName("18) REFACTOR - obtenerPostulantePorId debe retornar un detalle claro")
    void refactor_obtenerPostulantePorId_debeRetornarDetalleClaro() throws Exception {
        Postulante guardado = guardarPostulanteDePrueba("Carlos Vega", EstadoPostulante.POSTULADO);

        mockMvc.perform(get("/api/postulantes/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(guardado.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Carlos Vega")))
                .andExpect(jsonPath("$.email", is("carlos.vega@talento.edu")))
                .andExpect(jsonPath("$.estado", is("POSTULADO")))
                .andExpect(jsonPath("$.ofertaId", is(ofertaBase.getId().intValue())));
    }

    // ============================================================
    //                           UPDATE
    // ============================================================

    @Test
    @DisplayName("19) ROJO - actualizarPostulante debe fallar si no existe")
    void rojo_actualizarPostulante_debeFallarSiNoExiste() throws Exception {
        PostulanteDTO dto = dtoValido("Postulante Fantasma");

        mockMvc.perform(put("/api/postulantes/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("20) VERDE - actualizarPostulante debe modificar sus datos")
    void verde_actualizarPostulante_debeModificarDatos() throws Exception {
        Postulante original = guardarPostulanteDePrueba("Jose Sanchez", EstadoPostulante.POSTULADO);

        PostulanteDTO cambios = new PostulanteDTO();
        cambios.setNombre("Jose Sanchez Editado");
        cambios.setEmail("jose.editado@talento.edu");
        cambios.setTelefono("999333444");

        mockMvc.perform(put("/api/postulantes/{id}", original.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(original.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is("Jose Sanchez Editado")))
                .andExpect(jsonPath("$.email", is("jose.editado@talento.edu")));
    }

    @Test
    @DisplayName("21) REFACTOR - actualizarPostulante debe permitir actualizar el estado")
    void refactor_actualizarPostulante_debeActualizarEstado() throws Exception {
        Postulante original = guardarPostulanteDePrueba("Sara Rojas", EstadoPostulante.POSTULADO);

        PostulanteDTO cambios = new PostulanteDTO();
        cambios.setEstado("APROBADO");

        mockMvc.perform(put("/api/postulantes/{id}", original.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("APROBADO")))
                .andExpect(jsonPath("$.aprobado", is(true)));
    }

    // ============================================================
    //                           DELETE
    // ============================================================

    @Test
    @DisplayName("22) ROJO - eliminarPostulante debe fallar si no existe")
    void rojo_eliminarPostulante_debeFallarSiNoExiste() throws Exception {
        mockMvc.perform(delete("/api/postulantes/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("23) VERDE - eliminarPostulante debe eliminar uno existente")
    void verde_eliminarPostulante_debeEliminarExistente() throws Exception {
        Postulante postulante = guardarPostulanteDePrueba("Luis Mora", EstadoPostulante.POSTULADO);

        mockMvc.perform(delete("/api/postulantes/{id}", postulante.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("24) REFACTOR - postulante eliminado debe salir del listado de activos")
    void refactor_eliminarPostulante_debeMoverAHistorialOSalirDeActivos() throws Exception {
        Postulante activo = guardarPostulanteDePrueba("Diego Paz", EstadoPostulante.POSTULADO);

        // Confirmamos que aparece en /activos antes de eliminarlo.
        mockMvc.perform(get("/api/postulantes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + activo.getId() + ")]", hasSize(1)));

        mockMvc.perform(delete("/api/postulantes/{id}", activo.getId()))
                .andExpect(status().isNoContent());

        // Tras eliminarlo, ya no debe aparecer entre los activos.
        mockMvc.perform(get("/api/postulantes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + activo.getId() + ")]", hasSize(0)));
    }
}
