package com.rodrigo.delivery.delivery.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Loja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loja")
    private Long id;
    @Column(name = "nome")
    private String nome;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    private String telefone;
    private Boolean aberto;

    // Relacionamento com produtos
    @OneToMany(mappedBy = "loja")
    private List<Produto> produtos;
}
