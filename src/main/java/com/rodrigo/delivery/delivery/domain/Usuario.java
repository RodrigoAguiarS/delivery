package com.rodrigo.delivery.delivery.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    private String nome;
    private String email;
    private String senha;

    // Relacionamento com as vendas
    @OneToMany(mappedBy = "usuario")
    private List<Venda> vendas;

    // Relacionamento com a loja
    @ManyToOne
    @JoinColumn(name = "id_loja")
    private Loja loja;
}
