package com.rodrigo.delivery.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_venda")
    private Long id;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    // Relacionamento com a venda
    @ManyToOne
    @JoinColumn(name = "id_venda")
    private Venda venda;

    // Relacionamento com o produto
    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;
}

