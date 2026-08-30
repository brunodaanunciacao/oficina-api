package br.com.fiap.oficina.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityHeadersIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve conter o cabeçalho Cross-Origin-Resource-Policy na resposta")
    void deveConterCabecalhoCORP() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(header().string("Cross-Origin-Resource-Policy", "same-origin"));
    }

    @Test
    @DisplayName("Deve conter cabeçalhos de segurança HTTP em rotas da API")
    void deveConterCabecalhosDeSegurancaEmRotasDeApi() throws Exception {
        String body = """
                {
                  "email": "invalido@oficina.com",
                  "senha": "123"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(header().string("Cross-Origin-Resource-Policy", "same-origin"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("Content-Security-Policy", "default-src 'self'; frame-ancestors 'none'; form-action 'self';"));
    }
}
