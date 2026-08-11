package br.com.fiap.oficina.integration;

import br.com.fiap.oficina.domain.usuario.PerfilUsuario;
import br.com.fiap.oficina.domain.usuario.Usuario;
import br.com.fiap.oficina.infrastructure.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL = "teste.integracao@oficina.com";
    private static final String SENHA = "Teste@123";

    @BeforeEach
    void setUp() {

        usuarioRepository
                .findByEmail(EMAIL)
                .orElseGet(() -> {

                    Usuario usuario = new Usuario();

                    usuario.setNome("Usuário Integração");
                    usuario.setEmail(EMAIL);
                    usuario.setSenha(
                            passwordEncoder.encode(SENHA)
                    );
                    usuario.setPerfil(PerfilUsuario.ADMIN);
                    usuario.setAtivo(true);

                    return usuarioRepository.save(usuario);
                });
    }

    @Test
    void deveRealizarLoginComSucesso() throws Exception {

        String body = """
                {
                  "email": "teste.integracao@oficina.com",
                  "senha": "Teste@123"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void deveRetornar401QuandoSenhaForInvalida() throws Exception {

        String body = """
                {
                  "email": "teste.integracao@oficina.com",
                  "senha": "senha-errada"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(
                        jsonPath("$.message")
                                .value("E-mail ou senha inválidos")
                );
    }
}