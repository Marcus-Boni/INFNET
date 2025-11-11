package org.example.banco.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object que representa o saldo de uma conta bancária.
 * Este objeto é imutável e garante a precisão monetária usando BigDecimal.
 * 
 * Regras de negócio:
 * - Saldo não pode ser nulo
 * - Saldo não pode ser negativo
 * - Saldo deve ter no máximo 2 casas decimais
 * - Operações retornam novos objetos (imutabilidade)
 */
public final class Saldo {

    private static final int ESCALA_DECIMAL = 2;
    private static final BigDecimal VALOR_MINIMO = BigDecimal.ZERO;
    
    private final BigDecimal valor;

    /**
     * Constrói um Saldo validando as regras de negócio.
     *
     * @param valor o valor do saldo
     * @throws IllegalArgumentException se o saldo violar as regras de validação
     */
    private Saldo(final BigDecimal valor) {
        validarSaldo(valor);
        this.valor = valor.setScale(ESCALA_DECIMAL, RoundingMode.HALF_UP);
    }

    /**
     * Factory method para criar uma instância de Saldo a partir de BigDecimal.
     *
     * @param valor o valor do saldo
     * @return uma nova instância de Saldo
     * @throws IllegalArgumentException se o saldo for inválido
     */
    public static Saldo de(final BigDecimal valor) {
        return new Saldo(valor);
    }

    /**
     * Factory method para criar uma instância de Saldo a partir de Double.
     *
     * @param valor o valor do saldo
     * @return uma nova instância de Saldo
     * @throws IllegalArgumentException se o saldo for inválido
     */
    public static Saldo de(final Double valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do saldo não pode ser nulo");
        }
        return new Saldo(BigDecimal.valueOf(valor));
    }

    /**
     * Factory method para criar um Saldo inicial zerado.
     *
     * @return uma instância de Saldo com valor zero
     */
    public static Saldo zero() {
        return new Saldo(BigDecimal.ZERO);
    }

    private void validarSaldo(final BigDecimal saldo) {
        if (saldo == null) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        }

        if (saldo.compareTo(VALOR_MINIMO) < 0) {
            throw new IllegalArgumentException(
                "Saldo não pode ser negativo. Valor informado: " + saldo
            );
        }
    }

    /**
     * Command: adiciona um valor ao saldo atual, retornando um novo Saldo.
     *
     * @param valorAdicionar o valor a ser adicionado
     * @return um novo Saldo com o valor adicionado
     * @throws IllegalArgumentException se o valor for inválido
     */
    public Saldo adicionar(final BigDecimal valorAdicionar) {
        if (valorAdicionar == null) {
            throw new IllegalArgumentException("Valor a adicionar não pode ser nulo");
        }
        
        if (valorAdicionar.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor a adicionar não pode ser negativo");
        }

        return new Saldo(this.valor.add(valorAdicionar));
    }

    /**
     * Command: subtrai um valor do saldo atual, retornando um novo Saldo.
     *
     * @param valorSubtrair o valor a ser subtraído
     * @return um novo Saldo com o valor subtraído
     * @throws IllegalArgumentException se o valor for inválido ou resultar em saldo negativo
     */
    public Saldo subtrair(final BigDecimal valorSubtrair) {
        if (valorSubtrair == null) {
            throw new IllegalArgumentException("Valor a subtrair não pode ser nulo");
        }
        
        if (valorSubtrair.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor a subtrair não pode ser negativo");
        }

        final BigDecimal novoValor = this.valor.subtract(valorSubtrair);
        
        if (novoValor.compareTo(VALOR_MINIMO) < 0) {
            throw new IllegalArgumentException(
                String.format("Saldo insuficiente. Saldo atual: %s, Valor solicitado: %s",
                    this.valor, valorSubtrair)
            );
        }

        return new Saldo(novoValor);
    }

    /**
     * Query: verifica se o saldo é suficiente para uma determinada operação.
     *
     * @param valorNecessario o valor necessário
     * @return true se o saldo for suficiente, false caso contrário
     */
    public boolean isSuficientePara(final BigDecimal valorNecessario) {
        if (valorNecessario == null) {
            return false;
        }
        return this.valor.compareTo(valorNecessario) >= 0;
    }

    /**
     * Query: verifica se o saldo está zerado.
     *
     * @return true se o saldo for zero, false caso contrário
     */
    public boolean isZerado() {
        return this.valor.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Query: retorna o valor do saldo como BigDecimal.
     *
     * @return o valor do saldo
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * Query: retorna o valor do saldo como Double (para compatibilidade com banco de dados).
     *
     * @return o valor do saldo como Double
     */
    public Double getValorDouble() {
        return valor.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Saldo saldo = (Saldo) o;
        return valor.compareTo(saldo.valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return String.format("R$ %.2f", valor);
    }
}
