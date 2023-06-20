package com.rodrigo.delivery.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ClienteDto {
    @NotBlank(message = "O campo 'nome' é obrigatório.")
    @NotNull(message = "O campo 'nome' é obrigatório.")
    private String nome;

    @NotBlank(message = "O campo 'telefone' é obrigatório.")
    @NotNull(message = "O campo 'telefone' é obrigatório.")
    private String telefone;
    private EnderecoDto endereco;

}
