package com.rodrigo.delivery.delivery.domain;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    private String nome;

    private String descricao;

    @Column(precision = 10, scale = 2) // Exemplo de precisão e escala
    private BigDecimal preco;

    // Relacionamento com a loja
    @ManyToOne
    @JoinColumn(name = "id_loja")
    private Loja loja;

}

