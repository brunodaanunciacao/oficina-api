package br.com.fiap.oficina.interfaces.dtos;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone
) {
}