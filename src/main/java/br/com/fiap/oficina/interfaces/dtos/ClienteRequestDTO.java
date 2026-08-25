package br.com.fiap.oficina.interfaces.dtos;

import br.com.fiap.oficina.interfaces.validation.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF/CNPJ é obrigatório")
        @CpfCnpj(message = "CPF ou CNPJ inválido")
        String cpfCnpj,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone
) {
}