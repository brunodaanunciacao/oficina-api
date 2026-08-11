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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteApiIntegrationTest {

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
            "api.integracao@oficina.com";

    @BeforeEach
    void setUp() {

        Usuario usuario =
                usuarioRepository
                        .findByEmail(EMAIL)
                        .orElseGet(() -> {

                            Usuario novo =
                                    new Usuario();

                            novo.setNome(
                                    "Usuário API Integração"
                            );

                            novo.setEmail(EMAIL);

                            novo.setSenha(
                                    passwordEncoder.encode(
                                            "Teste@123"
                                    )
                            );

                            novo.setPerfil(
                                    PerfilUsuario.ADMIN
                            );

                            novo.setAtivo(true);

                            return usuarioRepository
                                    .save(novo);
                        });

        token = jwtService.gerarToken(
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }

    @Test
    void deveRetornar401QuandoAcessarClientesSemToken()
            throws Exception {

        mockMvc.perform(
                        get("/clientes")
                )
                .andExpect(
                        status().isUnauthorized()
                )
                .andExpect(
                        jsonPath("$.status")
                                .value(401)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Unauthorized")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Autenticação necessária"
                                )
                );
    }

    @Test
    void deveAcessarClientesComTokenValido()
            throws Exception {

        mockMvc.perform(
                        get("/clientes")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        "application/json"
                                )
                );
    }
}