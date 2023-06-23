package com.rodrigo.delivery.error;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Mensagens {
    private List<String> erros;
    public void adicionarErro(String mensagem) {
        if (erros == null) {
            erros = new ArrayList<>();
        }
        erros.add(mensagem);
    }
    public Mensagens(List<String> erros) {
        this.erros = erros;
    }
    public boolean possuiErros() {
        return erros != null && !erros.isEmpty();
    }
}
