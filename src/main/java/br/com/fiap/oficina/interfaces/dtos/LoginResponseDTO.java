package br.com.fiap.oficina.interfaces.dtos;

public record LoginResponseDTO(

        String token,
        String tipo,
        Long expiresIn

) {
}