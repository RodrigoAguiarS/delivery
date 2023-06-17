package com.rodrigo.delivery.delivery.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long id;
    private String rua;
    private String bairro;
    @Column(name = "numero_casa")
    private String numeroCasa;
    private String cep;

}
