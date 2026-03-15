package edu.hpi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hpi.application.InstitutionService;
import edu.hpi.domain.Institution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InstitutionController.class)
@AutoConfigureMockMvc
@Import(InstitutionControllerTest.TestSecurityConfig.class)
class InstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InstitutionService institutionService;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Nested
    @DisplayName("POST /api/institutions")
    class CreateInstitutionTests {

        @Test
        void shouldCreateInstitutionSuccessfully() throws Exception {
            Institution institution = new Institution("Institución API");
            String requestBody = """
                    {
                      "name": "Institución API"
                    }
                    """;

            BDDMockito.given(institutionService.create("Institución API"))
                    .willReturn(institution);

            mockMvc.perform(post("/api/institutions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(institution.getId().toString()))
                    .andExpect(jsonPath("$.name").value("Institución API"));
        }

        @Test
        void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
            String requestBody = """
                    {
                      "name": "   "
                    }
                    """;

            mockMvc.perform(post("/api/institutions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
            String requestBody = """
                    {
                    }
                    """;

            mockMvc.perform(post("/api/institutions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequestWhenBodyIsInvalidJson() throws Exception {
            String requestBody = """
                    {
                      "name":
                    }
                    """;

            mockMvc.perform(post("/api/institutions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }
}
