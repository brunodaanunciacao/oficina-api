package br.com.fiap.oficina.interfaces.controllers;

import br.com.fiap.oficina.application.services.AutenticacaoService;
import br.com.fiap.oficina.interfaces.dtos.LoginRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticacaoService autenticacaoService;

    public AuthController(
            AutenticacaoService autenticacaoService) {

        this.autenticacaoService =
                autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(
                autenticacaoService.login(request)
        );
    }
}