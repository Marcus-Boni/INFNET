package org.example.banco.exceptions;

/**
 * Exceção lançada quando uma operação bancária inválida é solicitada.
 * Esta exceção cobre casos como valores negativos, parâmetros nulos,
 * ou outras violações de regras de negócio.
 */
public class OperacaoInvalidaException extends RuntimeException {

    /**
     * Constrói uma nova exceção com a mensagem especificada.
     *
     * @param mensagem a mensagem descrevendo a operação inválida
     */
    public OperacaoInvalidaException(final String mensagem) {
        super(mensagem);
    }

    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     *
     * @param mensagem a mensagem descrevendo a operação inválida
     * @param causa a causa da exceção
     */
    public OperacaoInvalidaException(final String mensagem, final Throwable causa) {
        super(mensagem, causa);
    }
}
