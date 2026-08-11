package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.config.JwtService;
import br.com.fiap.oficina.domain.usuario.Usuario;
import br.com.fiap.oficina.infrastructure.repositories.UsuarioRepository;
import br.com.fiap.oficina.interfaces.dtos.LoginRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.LoginResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.CredenciaisInvalidasException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private Long expiration;

    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(
            LoginRequestDTO request) {

        Usuario usuario =
                usuarioRepository
                        .findByEmail(request.email())
                        .orElseThrow(() ->
                                new CredenciaisInvalidasException(
                                        "E-mail ou senha inválidos"
                                )
                        );

        if (!Boolean.TRUE.equals(usuario.getAtivo())) {

            throw new CredenciaisInvalidasException(
                    "Usuário inativo"
            );
        }

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenha())) {

            throw new CredenciaisInvalidasException(
                    "E-mail ou senha inválidos"
            );
        }

        String token =
                jwtService.gerarToken(
                        usuario.getEmail(),
                        usuario.getPerfil().name()
                );

        return new LoginResponseDTO(
                token,
                "Bearer",
                expiration / 1000
        );
    }
}