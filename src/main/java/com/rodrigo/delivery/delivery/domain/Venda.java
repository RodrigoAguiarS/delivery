package com.rodrigo.delivery.delivery.domain;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(precision = 10, scale = 2) // Exemplo de precisão e escala
    private BigDecimal valorTotal;

    // Relacionamento com o usuário
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Relacionamento com o cliente
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    // Relacionamento com os itens de venda
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itensVenda = new ArrayList<>();
}

