package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequestDTO(

        @NotBlank(message = "Placa é obrigatória")
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