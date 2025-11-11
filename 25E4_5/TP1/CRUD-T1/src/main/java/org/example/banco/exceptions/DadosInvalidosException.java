package org.example.banco.exceptions;

/**
 * Exceção lançada quando dados inválidos são fornecidos ao sistema.
 * Útil para validações de entrada e dados de formulários.
 */
public class DadosInvalidosException extends RuntimeException {

    /**
     * Constrói uma nova exceção com a mensagem especificada.
     *
     * @param mensagem a mensagem descrevendo os dados inválidos
     */
    public DadosInvalidosException(final String mensagem) {
        super(mensagem);
    }

    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     *
     * @param mensagem a mensagem descrevendo os dados inválidos
     * @param causa a causa da exceção
     */
    public DadosInvalidosException(final String mensagem, final Throwable causa) {
        super(mensagem, causa);
    }
}
