package com.rodrigo.delivery.delivery.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;
    private String nome;
    // Relacionamento com o endereço
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    private String telefone;

    // Relacionamento com as compras
    @OneToMany(mappedBy = "cliente")
    private List<Venda> compras;
}
