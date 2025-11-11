package org.example.banco.exceptions;

/**
 * Exceção lançada quando uma conta bancária não é encontrada no sistema.
 * Esta é uma exceção de negócio que indica que a operação solicitada
 * não pode ser realizada porque a conta não existe.
 */
public class ContaNotFoundException extends RuntimeException {

    private final Long contaId;

    /**
     * Constrói uma nova exceção com o ID da conta não encontrada.
     *
     * @param contaId o ID da conta que não foi encontrada
     */
    public ContaNotFoundException(final Long contaId) {
        super(String.format("Conta com ID %d não foi encontrada no sistema", contaId));
        this.contaId = contaId;
    }

    /**
     * Constrói uma nova exceção com uma mensagem personalizada.
     *
     * @param mensagem a mensagem de erro
     */
    public ContaNotFoundException(final String mensagem) {
        super(mensagem);
        this.contaId = null;
    }

    /**
     * Retorna o ID da conta que não foi encontrada.
     *
     * @return o ID da conta, ou null se não especificado
     */
    public Long getContaId() {
        return contaId;
    }
}
