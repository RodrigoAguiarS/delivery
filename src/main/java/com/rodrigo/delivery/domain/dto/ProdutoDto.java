package com.rodrigo.delivery.domain.dto;

import com.rodrigo.delivery.domain.Categoria;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProdutoDto {

    @NotBlank(message = "O campo 'nome' é obrigatório.")
    @NotNull(message = "O campo 'nome' é obrigatório.")
    private String nome;

    @NotBlank(message = "O campo 'descricao' é obrigatório.")
    @NotNull(message = "O campo 'descricao' é obrigatório.")
    private String descricao;

    @NotNull(message = "O campo 'categoria' é obrigatório.")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal preco;
}
