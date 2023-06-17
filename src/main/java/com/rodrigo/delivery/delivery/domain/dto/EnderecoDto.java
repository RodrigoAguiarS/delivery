package com.rodrigo.delivery.delivery.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class EnderecoDto {
    @JsonIgnore
    private String rua;

    private String numeroCasa;
    @JsonIgnore
    private String bairro;
    @NotBlank(message = "O campo 'cep' é obrigatório.")
    @NotNull(message = "O campo 'cep' é obrigatório.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O campo 'cep' deve estar no formato 12345-678.")
    private String cep;
}
