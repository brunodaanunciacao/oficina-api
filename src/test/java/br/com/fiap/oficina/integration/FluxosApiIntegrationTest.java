package br.com.fiap.oficina.integration;

import br.com.fiap.oficina.config.JwtService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FluxosApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private String token;

    private static final String EMAIL =
            "fluxo.integracao@oficina.com";

    @BeforeEach
    void setUp() {

        Usuario usuario =
                usuarioRepository
                        .findByEmail(EMAIL)
                        .orElseGet(() -> {

                            Usuario novo = new Usuario();

                            novo.setNome("Usuário Fluxo Integração");
                            novo.setEmail(EMAIL);
                            novo.setSenha(
                                    passwordEncoder.encode("Teste@123")
                            );
                            novo.setPerfil(PerfilUsuario.ADMIN);
                            novo.setAtivo(true);

                            return usuarioRepository.save(novo);
                        });

        token = jwtService.gerarToken(
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }

    @Test
    void deveRetornar400AoCriarClienteInvalido()
            throws Exception {

        String body = """
                {
                  "nome": "",
                  "cpfCnpj": "",
                  "email": "email-invalido",
                  "telefone": ""
                }
                """;

        mockMvc.perform(
                        post("/clientes")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deveRetornar404AoBuscarClienteInexistente()
            throws Exception {

        mockMvc.perform(
                        get("/clientes/999999")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value("Cliente não encontrado")
                );
    }

    @Test
    void deveRetornar404AoBuscarPecaInexistente()
            throws Exception {

        mockMvc.perform(
                        get("/pecas/999999")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value("Peça não encontrada")
                );
    }

    @Test
    void deveRetornar404AoBuscarOrdemServicoInexistente()
            throws Exception {

        mockMvc.perform(
                        get("/ordens-servico/999999")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Ordem de serviço não encontrada"
                                )
                );
    }
}