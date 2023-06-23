package com.rodrigo.delivery.error;

public class MensagensErroException extends RuntimeException {
    private final Mensagens mensagens;
    public MensagensErroException(Mensagens mensagens) {
        this.mensagens = mensagens;
    }
    public Mensagens getMensagens() {
        return mensagens;
    }
}
