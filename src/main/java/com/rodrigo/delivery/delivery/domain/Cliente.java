package com.rodrigo.delivery.delivery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Pessoa {
    @Column(name = "telefone", unique = true)
    private String telefone;

}
