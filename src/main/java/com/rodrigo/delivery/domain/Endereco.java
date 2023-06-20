package com.rodrigo.delivery.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long id;
    private String rua;
    private String bairro;

    @NotBlank(message = "O campo 'Número da Casa' é obrigatório.")
    @NotNull(message = "O campo 'Número da Casa' é obrigatório.")
    @Column(name = "numero_casa")
    private String numeroCasa;

    @NotBlank(message = "O campo 'cep' é obrigatório.")
    @NotNull(message = "O campo 'cep' é obrigatório.")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O campo 'cep' deve estar no formato 12345-678.")
    private String cep;

}
