package br.edu.infnet.logistica.excecao;

public class EntregaInvalidaException extends RuntimeException {
    public EntregaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
