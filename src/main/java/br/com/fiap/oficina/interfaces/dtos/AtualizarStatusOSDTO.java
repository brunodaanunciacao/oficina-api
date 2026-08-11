package br.com.fiap.oficina.interfaces.dtos;

import br.com.fiap.oficina.domain.ordemservico.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusOSDTO(

        @NotNull(message = "Status é obrigatório")
        StatusOrdemServico status

) {
}