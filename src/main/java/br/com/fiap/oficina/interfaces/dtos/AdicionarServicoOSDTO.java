package br.com.fiap.oficina.interfaces.dtos;

import jakarta.validation.constraints.NotNull;

public record AdicionarServicoOSDTO(

        @NotNull(message = "Serviço é obrigatório")
        Long servicoId

) {
}