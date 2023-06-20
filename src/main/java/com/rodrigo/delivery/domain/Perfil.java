package com.rodrigo.delivery.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Perfil implements GrantedAuthority {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    public static final String PERFIL_ADMIN = "ADMIN";
    public static final String PERFIL_USUARIO = "USUARIO";
    public static final String PERFIL_CLIENTE = "CLIENTE";
    @Override
    public String getAuthority() {
        return nome;
    }
}
