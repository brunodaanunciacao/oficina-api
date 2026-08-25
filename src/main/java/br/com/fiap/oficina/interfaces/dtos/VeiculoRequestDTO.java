package br.com.fiap.oficina.interfaces.dtos;

import br.com.fiap.oficina.interfaces.validation.PlacaVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequestDTO(

        @NotBlank(message = "Placa é obrigatória")
        @PlacaVeiculo(message = "Placa de veículo inválida (deve seguir o formato tradicional ABC1234 ou Mercosul ABC1D23)")
        String placa,

        @NotBlank(message = "Marca é obrigatória")
        String marca,

        @NotBlank(message = "Modelo é obrigatório")
        String modelo,

        @NotNull(message = "Ano é obrigatório")
        Integer ano,

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId
) {
}