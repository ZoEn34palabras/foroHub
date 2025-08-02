package com.forohub.controller;

import com.forohub.dto.TopicResponse;
import com.forohub.security.JwtTokenFilter;
import com.forohub.service.TopicService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureJsonTesters
@WebMvcTest(
        controllers = TopicController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = { JwtTokenFilter.class }
        )
)
@AutoConfigureMockMvc(addFilters = false)
@Import(TopicControllerTest.TestConfig.class) // Importa configuración personalizada para tests
class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TopicService topicService; // Este es el mock definido en TestConfig

    // Configuración de beans mock para pruebas
    @TestConfiguration
    static class TestConfig {
        @Bean
        public TopicService topicService() {
            return Mockito.mock(TopicService.class);
        }
    }

    @Test
    @DisplayName("GET /topics debería retornar los tópicos paginados")
    void getAllTopics_returnsPaginatedTopics() throws Exception {
        // Arrange – Prepara los datos de prueba simulando un tópico real
        TopicResponse topic = new TopicResponse(
                1L,
                "Ayuda con Spring Boot",
                "Estoy teniendo errores de compilación",
                Instant.parse("2025-08-01T23:46:45.569476Z"),
                "OPEN",
                "Usuario Básico",
                "Java Web"
        );

        // Define un objeto Pageable que coincida con la configuración por defecto del controlador
        Pageable pageable = PageRequest.of(0, 10, Sort.by("fechaCreacion").descending());
        Page<TopicResponse> page = new PageImpl<>(List.of(topic), pageable, 1);

        // Configura el mock para retornar los datos simulados
        when(topicService.listTopics(any(Pageable.class))).thenReturn(page);

        // Act & Assert – Realiza la petición HTTP y valida la respuesta
        MvcResult result = mockMvc.perform(get("/topics")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "fechaCreacion,desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Código HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Tipo de contenido JSON
                .andExpect(jsonPath("$.content").isArray()) // Verifica que el campo 'content' es un arreglo
                .andExpect(jsonPath("$.content[0].titulo").value("Ayuda con Spring Boot"))
                .andExpect(jsonPath("$.content[0].authorName").value("Usuario Básico"))
                .andExpect(jsonPath("$.content[0].courseName").value("Java Web"))
                .andExpect(jsonPath("$.totalElements").value(1)) // Metadatos de paginación
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andReturn();

        // Verificación adicional con AssertJ (más legible)
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).contains("Ayuda con Spring Boot");
        assertThat(jsonResponse).contains("Usuario Básico");
        assertThat(jsonResponse).contains("Java Web");

        // Verifica que el servicio fue invocado una sola vez
        verify(topicService, times(1)).listTopics(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /topics debería manejar excepciones del servicio correctamente")
    void getAllTopics_handlesServiceException() throws Exception {
        // Configura el mock para lanzar una excepción cuando se llame
        doThrow(new RuntimeException("Error en la base de datos"))
                .when(topicService)
                .listTopics(any(Pageable.class));

        // Act & Assert – Realiza la petición y espera un error 500
        mockMvc.perform(get("/topics")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        // Verifica que el servicio fue llamado una vez (a pesar del error)
        verify(topicService, atLeastOnce()) .listTopics(any(Pageable.class));
    }
}
