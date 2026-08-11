package br.com.fiap.oficina.interfaces.dtos;

public record VeiculoResponseDTO(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        Long clienteId,
        String clienteNome
) {
}