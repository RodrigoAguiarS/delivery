package com.rodrigo.delivery.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioDto {
    private String nome;
    private EnderecoDto endereco;
    private String email;
    private String senha;
    private List<PerfilDto> perfis;
}
