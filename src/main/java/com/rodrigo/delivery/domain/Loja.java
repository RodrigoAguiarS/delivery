package com.rodrigo.delivery.domain;

import lombok.Data;

import javax.persistence.*;

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

}
