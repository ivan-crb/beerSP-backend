package com.beersp.is2.beerspbackend;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import com.beersp.is2.beerspbackend.controller.CervezaController;
import com.beersp.is2.beerspbackend.controller.LocalController;
import com.beersp.is2.beerspbackend.controller.UsuarioController;
import com.beersp.is2.beerspbackend.model.Cerveza;
import com.beersp.is2.beerspbackend.model.Local;
import com.beersp.is2.beerspbackend.model.Usuario;
import com.beersp.is2.beerspbackend.repository.CervezaRepository;
import com.beersp.is2.beerspbackend.repository.LocalRepository;
import com.beersp.is2.beerspbackend.service.CervezaService;
import com.beersp.is2.beerspbackend.service.LocalService;
import com.beersp.is2.beerspbackend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LocalController.class)
@Import(LocalControllerTest.MockConfig.class)
class LocalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocalService localService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {

        @Bean
        LocalRepository localRepository() {
            return mock(LocalRepository.class);
        }

        @Bean
        LocalService localService(LocalRepository localRepository) {
            return mock(LocalService.class);
        }
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reset(localService);
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /locales")
    class ObtenerLocalesTests {

        @Test
        @DisplayName("Debe devolver lista de locales")
        void obtenerLocales_ok() throws Exception {
            List<Local> locales = Arrays.asList(
                    new Local(1, "Local 1","dir1", Date.valueOf("2025-11-16")),
                    new Local(2, "Local 2","dir2", Date.valueOf("2025-11-16"))
            );

            when(localService.obtenerLocales()).thenReturn(locales);

            mockMvc.perform(get("/locales"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /locales/{id}")
    class ObtenerLocalTests {

        @Test
        @DisplayName("Debe devolver un local existente")
        void obtenerLocal_existente() throws Exception {
            Local local = new Local(1, "Local 1","dir1", Date.valueOf("2025-11-16"));

            when(localService.existeLocal(1)).thenReturn(true);
            when(localService.obtenerLocal(1)).thenReturn(local);

            mockMvc.perform(get("/locales/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void obtenerLocal_noExiste() throws Exception {
            when(localService.existeLocal(1)).thenReturn(false);

            mockMvc.perform(get("/locales/1"))
                    .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("POST /locales")
    class CrearLocalTests {

        @Test
        @DisplayName("Debe crear local y retornar 201")
        void crearLocal() throws Exception {
            Local nuevo = new Local(1, "Local 1","dir1", Date.valueOf("2025-11-16"));
            Local creado = new Local(10, "Local 2","dir2", Date.valueOf("2025-11-16"));

            when(localService.crearLocal(any(Local.class))).thenReturn(creado);

            mockMvc.perform(post("/locales")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevo)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/locales/10"));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("PUT /locales/{id}")
    class ActualizarLocalTests {

        @Test
        @DisplayName("Debe actualizar y retornar 204")
        void actualizar_ok() throws Exception {
            Local mod = new Local(1, "Local 1","dir111", Date.valueOf("2025-11-16"));

            when(localService.existeLocal(1)).thenReturn(true);

            mockMvc.perform(put("/locales/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mod)))
                    .andExpect(status().isNoContent());

            verify(localService).actualizarLocal(eq(1), any(Local.class));
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void actualizar_noExiste() throws Exception {
            when(localService.existeLocal(1)).thenReturn(false);

            mockMvc.perform(put("/locales/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Local())))
                    .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("DELETE /locales/{id}")
    class EliminarLocalTests {

        @Test
        @DisplayName("Debe eliminar y retornar 204")
        void eliminar_ok() throws Exception {
            when(localService.existeLocal(1)).thenReturn(true);

            mockMvc.perform(delete("/locales/1"))
                    .andExpect(status().isNoContent());

            verify(localService).eliminarLocal(1);
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void eliminar_noExiste() throws Exception {
            when(localService.existeLocal(1)).thenReturn(false);

            mockMvc.perform(delete("/locales/1"))
                    .andExpect(status().isNotFound());
        }
    }
}
@WebMvcTest(CervezaController.class)
@Import(CervezaControllerTest.MockConfig.class)
class CervezaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CervezaService cervezaService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {

        @Bean
        CervezaRepository cervezaRepository() {
            return mock(CervezaRepository.class);
        }

        @Bean
        CervezaService cervezaService(CervezaRepository cervezaRepository) {
            return mock(CervezaService.class);
        }
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reset(cervezaService);
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /cervezas")
    class ObtenerCervezasTests {

        @Test
        @DisplayName("Debe devolver lista de cervezas")
        void obtenerCervezas_ok() throws Exception {
            List<Cerveza> cervezas = Arrays.asList(
                    new Cerveza(1, "Cerveza1","foto1", "descripción1","estilo1","procedencia1","tamaño1","formato1", 1.0, 1, "color1", Date.valueOf("2025-11-16")),
                    new Cerveza(2, "Cerveza2","foto2", "descripción2","estilo2","procedencia2","tamaño2","formato2", 2.0, 2, "color2", Date.valueOf("2025-11-16"))
            );

            when(cervezaService.obtenerCervezas()).thenReturn(cervezas);

            mockMvc.perform(get("/cervezas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /cervezas/{id}")
    class ObtenerCervezaTests {

        @Test
        @DisplayName("Debe devolver una cerveza existente")
        void obtenerCerveza_existente() throws Exception {
            Cerveza cerveza = new Cerveza(1, "Cerveza1","foto1", "descripción1","estilo1","procedencia1","tamaño1","formato1", 1.0, 1, "color1", Date.valueOf("2025-11-16"));

            when(cervezaService.existeCerveza(1)).thenReturn(true);
            when(cervezaService.obtenerCerveza(1)).thenReturn(cerveza);

            mockMvc.perform(get("/cervezas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void obtenerCerveza_noExiste() throws Exception {
            when(cervezaService.existeCerveza(1)).thenReturn(false);

            mockMvc.perform(get("/cervezas/1"))
                    .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("POST /cervezas")
    class CrearCervezaTests {

        @Test
        @DisplayName("Debe crear cerveza y retornar 201")
        void crearCerveza() throws Exception {
            Cerveza nueva = new Cerveza(3, "Cerveza3","foto3", "descripción3","estilo3","procedencia3","tamaño3","formato3", 3.0, 3, "color3",Date.valueOf("2025-11-16"));
            Cerveza creada = new Cerveza(10, "Cerveza10","foto10", "descripción10","estilo10","procedencia10","tamaño10","formato10", 10.0, 10, "color10", Date.valueOf("2025-11-16"));

            when(cervezaService.crearCerveza(any(Cerveza.class))).thenReturn(creada);

            mockMvc.perform(post("/cervezas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nueva)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/cervezas/10"));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("PUT /cervezas/{id}")
    class ActualizarCervezaTests {

        @Test
        @DisplayName("Debe actualizar y retornar 204")
        void actualizar_ok() throws Exception {
            Cerveza mod = new Cerveza(1, "Cerveza1","foto1", "amarga en exceso","estilo1","procedencia1","tamaño1","formato1", 1.0, 1, "color1", Date.valueOf("2025-11-16"));

            when(cervezaService.existeCerveza(1)).thenReturn(true);

            mockMvc.perform(put("/cervezas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mod)))
                    .andExpect(status().isNoContent());

            verify(cervezaService).actualizarCerveza(eq(1), any(Cerveza.class));
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void actualizar_noExiste() throws Exception {
            when(cervezaService.existeCerveza(1)).thenReturn(false);

            mockMvc.perform(put("/cervezas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Cerveza())))
                    .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("DELETE /cervezas/{id}")
    class EliminarCervezaTests {

        @Test
        @DisplayName("Debe eliminar y retornar 204")
        void eliminar_ok() throws Exception {
            when(cervezaService.existeCerveza(1)).thenReturn(true);

            mockMvc.perform(delete("/cervezas/1"))
                    .andExpect(status().isNoContent());

            verify(cervezaService).eliminarCerveza(1);
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void eliminar_noExiste() throws Exception {
            when(cervezaService.existeCerveza(1)).thenReturn(false);

            mockMvc.perform(delete("/cervezas/1"))
                    .andExpect(status().isNotFound());
        }
    }
}
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        reset(usuarioService);
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /usuarios")
    class ObtenerUsuariosTests {

        @Test
        @DisplayName("Debe devolver lista de usuarios")
        void obtenerUsuarios_ok() throws Exception {
            List<Usuario> usuarios = Arrays.asList(
                    new Usuario(1, "user1", Date.valueOf("2025-11-17"), "pass1", "email1", "nombre1", "apellidos1", "foto1", "procedencia1", "introduccion1"),
                    new Usuario(2, "user2", Date.valueOf("2025-10-09"), "pass2", "email2", "nombre2", "apellidos2", "foto2", "procedencia2", "introduccion2")
            );

            when(usuarioService.obtenerUsuarios()).thenReturn(usuarios);

            mockMvc.perform(get("/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /usuarios/username/{username}")
    class ObtenerUsuarioPorUsernameTests {

        @Test
        @DisplayName("Debe devolver usuario existente por username")
        void obtenerUsuarioPorUsername_ok() throws Exception {
            Usuario u = new Usuario(1, "user1", Date.valueOf("2025-11-17"), "pass1", "email1", "nombre1", "apellidos1", "foto1", "procedencia1", "introduccion1");

            when(usuarioService.existeUsuario("user1")).thenReturn(true);
            when(usuarioService.obtenerUsuario("user1")).thenReturn(u);

            mockMvc.perform(get("/usuarios/username/user1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void obtenerUsuarioPorUsername_noExiste() throws Exception {
            when(usuarioService.existeUsuario("user1")).thenReturn(false);

            mockMvc.perform(get("/usuarios/username/user1"))
                    .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /usuarios/{id}")
    class ObtenerUsuarioPorIdTests {

        @Test
        @DisplayName("Debe devolver usuario existente")
        void obtenerUsuarioPorId_ok() throws Exception {
            Usuario u = new Usuario(1, "user1", Date.valueOf("2025-11-17"), "pass1", "email1", "nombre1", "apellidos1", "foto1", "procedencia1", "introduccion1");

            when(usuarioService.existeUsuario(1)).thenReturn(true);
            when(usuarioService.obtenerUsuario(1)).thenReturn(u);

            mockMvc.perform(get("/usuarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Debe devolver 404 si no existe")
        void obtenerUsuarioPorId_noExiste() throws Exception {
            when(usuarioService.existeUsuario(1)).thenReturn(false);

            mockMvc.perform(get("/usuarios/1"))
                    .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("POST /usuarios")
    class CrearUsuarioTests {

        @Test
        @DisplayName("Debe crear usuario y retornar 201")
        void crearUsuario_ok() throws Exception {
            Usuario nuevo = new Usuario(4, "user4", Date.valueOf("2025-11-17"), "pass4", "email4", "nombre4", "apellidos4", "foto4", "procedencia4", "introduccion4");
            Usuario creado = new Usuario(10, "user10", Date.valueOf("2025-11-17"), "pass10", "email10", "nombre10", "apellidos10", "foto10", "procedencia10", "introduccion10");

            when(usuarioService.existeUsuario("newUser")).thenReturn(false);
            when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(creado);

            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevo)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/usuarios/10"));
        }

        @Test
        @DisplayName("Debe devolver 400 si username ya existe")
        void crearUsuario_usernameExiste() throws Exception {
            Usuario nuevo = new Usuario(null, "user1", Date.valueOf("2025-11-17"), "pass1", "email1", "nombre1", "apellidos1", "foto1", "procedencia1", "introduccion1");

            when(usuarioService.existeUsuario("user1")).thenReturn(true);

            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevo)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("PUT /usuarios/{id}")
    class ActualizarUsuarioTests {

        @Test
        @DisplayName("Debe actualizar y retornar 204")
        void actualizar_ok() throws Exception {
            Usuario mod = new Usuario(1, "newuser", Date.valueOf("2025-11-17"), "pass1", "newemail1", "nombre1", "apellidos1", "newfoto1", "procedencia1", "introduccion1");

            when(usuarioService.existeUsuario(1)).thenReturn(true);
            when(usuarioService.existeUsuario("nuevoUser")).thenReturn(false);

            mockMvc.perform(put("/usuarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mod)))
                    .andExpect(status().isNoContent());

            verify(usuarioService).actualizarUsuario(eq(1), any(Usuario.class));
        }

        @Test
        @DisplayName("Debe devolver 404 si id no existe")
        void actualizar_noExiste() throws Exception {
            when(usuarioService.existeUsuario(1)).thenReturn(false);

            mockMvc.perform(put("/usuarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Usuario())))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe devolver 400 si username ya existe en otro usuario")
        void actualizar_usernameDuplicado() throws Exception {

            Usuario mod = new Usuario(
                    1,
                    "usuarioDuplicado",
                    Date.valueOf("2025-11-17"),
                    "pass1",
                    "email1",
                    "nombre1",
                    "apellidos1",
                    "foto1",
                    "procedencia1",
                    "introduccion1"
            );

            // El usuario actual que ya existe en el sistema con ese ID
            Usuario usuarioActual = new Usuario(
                    1,
                    "user1",
                    Date.valueOf("2025-11-17"),
                    "pass1",
                    "email1",
                    "nombre1",
                    "apellidos1",
                    "foto1",
                    "procedencia1",
                    "introduccion1"
            );

            when(usuarioService.existeUsuario(1)).thenReturn(true);
            when(usuarioService.existeUsuario("usuarioDuplicado")).thenReturn(true);
            when(usuarioService.obtenerUsuario(1)).thenReturn(usuarioActual);

            mockMvc.perform(put("/usuarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mod)))
                    .andExpect(status().isBadRequest());
        }

        // ---------------------------------------------------------------------
        @Nested
        @DisplayName("DELETE /usuarios/{id}")
        class EliminarUsuarioTests {

            @Test
            @DisplayName("Debe eliminar y retornar 204")
            void eliminar_ok() throws Exception {
                when(usuarioService.existeUsuario(1)).thenReturn(true);

                mockMvc.perform(delete("/usuarios/1"))
                        .andExpect(status().isNoContent());

                verify(usuarioService).eliminarUsuario(1);
            }

            @Test
            @DisplayName("Debe devolver 404 si no existe")
            void eliminar_noExiste() throws Exception {
                when(usuarioService.existeUsuario(1)).thenReturn(false);

                mockMvc.perform(delete("/usuarios/1"))
                        .andExpect(status().isNotFound());
            }
        }
        @Test
        @DisplayName("PUT /usuarios/{id} debe actualizar cuando username es null")
        void actualizar_sinUsername() throws Exception {

            Usuario mod = new Usuario(
                    1,
                    null,
                    Date.valueOf("2025-11-17"),
                    "pass1",
                    "email1",
                    "nombre1",
                    "apellidos1",
                    "foto1",
                    "proc",
                    "intro"
            );

            when(usuarioService.existeUsuario(1)).thenReturn(true);

            mockMvc.perform(put("/usuarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mod)))
                    .andExpect(status().isNoContent());
        }
        @Test
        @DisplayName("PUT /usuarios/{id} debe permitir username igual al actual")
        void actualizar_usernameEsMismo() throws Exception {

            Usuario mod = new Usuario(
                    1,
                    "user1",
                    Date.valueOf("2025-11-17"),
                    "pass1",
                    "email1",
                    "nombre1",
                    "apellidos1",
                    "foto1",
                    "proc",
                    "intro"
            );

            Usuario usuarioActual = new Usuario(
                    1,
                    "user1",
                    Date.valueOf("2025-11-17"),
                    "pass1",
                    "email1",
                    "nombre1",
                    "apellidos1",
                    "foto1",
                    "proc",
                    "intro"
            );

            when(usuarioService.existeUsuario(1)).thenReturn(true);
            when(usuarioService.existeUsuario("user1")).thenReturn(true);
            when(usuarioService.obtenerUsuario(1)).thenReturn(usuarioActual);

            mockMvc.perform(put("/usuarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mod)))
                    .andExpect(status().isNoContent());
        }


    }
}

@SpringBootTest
class BeerspBackendApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainEjecutaSpringApplication() {
        BeerspBackendApplication.main(new String[] {});
    }
}