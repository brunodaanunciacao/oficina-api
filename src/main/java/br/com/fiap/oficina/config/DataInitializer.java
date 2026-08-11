package br.com.fiap.oficina.config;

import br.com.fiap.oficina.domain.usuario.PerfilUsuario;
import br.com.fiap.oficina.domain.usuario.Usuario;
import br.com.fiap.oficina.infrastructure.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner criarUsuarioAdmin(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.password}") String senha) {

        return args -> {

            if (!usuarioRepository.existsByEmail(email)) {

                Usuario usuario =
                        new Usuario();

                usuario.setNome(
                        "Administrador"
                );

                usuario.setEmail(
                        email
                );

                usuario.setSenha(
                        passwordEncoder.encode(senha)
                );

                usuario.setPerfil(
                        PerfilUsuario.ADMIN
                );

                usuario.setAtivo(
                        true
                );

                usuarioRepository.save(
                        usuario
                );
            }
        };
    }
}