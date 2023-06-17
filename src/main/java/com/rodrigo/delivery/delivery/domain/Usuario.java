package com.rodrigo.delivery.delivery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Usuario extends Pessoa {
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
