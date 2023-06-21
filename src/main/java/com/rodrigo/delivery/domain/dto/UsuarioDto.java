package com.rodrigo.delivery.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioDto {
    private String nome;
    private EnderecoDto endereco;
    private String email;
    private String senha;
    @JsonIgnore
    private List<PerfilDto> perfis;
}
