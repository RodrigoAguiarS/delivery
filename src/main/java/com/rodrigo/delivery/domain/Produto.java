package com.rodrigo.delivery.domain;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;
    private String nome;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Column(precision = 10, scale = 2) // Exemplo de precisão e escala
    private BigDecimal preco;

}

