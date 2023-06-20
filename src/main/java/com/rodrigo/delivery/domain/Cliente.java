package com.rodrigo.delivery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Pessoa {
    @Column(name = "telefone", unique = true)
    private String telefone;

}
