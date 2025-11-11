package org.example.banco.exceptions;

import java.math.BigDecimal;

/**
 * Exceção lançada quando uma operação não pode ser realizada devido a saldo insuficiente.
 * Esta é uma exceção de negócio que protege a integridade das regras de saldo.
 */
public class SaldoInsuficienteException extends RuntimeException {

    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;

    /**
     * Constrói uma nova exceção com informações sobre saldo e valor solicitado.
     *
     * @param saldoAtual o saldo atual da conta
     * @param valorSolicitado o valor que foi solicitado
     */
    public SaldoInsuficienteException(final BigDecimal saldoAtual, final BigDecimal valorSolicitado) {
        super(String.format(
            "Saldo insuficiente para realizar a operação. Saldo disponível: R$ %.2f, Valor solicitado: R$ %.2f",
            saldoAtual, valorSolicitado
        ));
        this.saldoAtual = saldoAtual;
        this.valorSolicitado = valorSolicitado;
    }

    /**
     * Retorna o saldo atual da conta.
     *
     * @return o saldo atual
     */
    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    /**
     * Retorna o valor que foi solicitado.
     *
     * @return o valor solicitado
     */
    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }
}
