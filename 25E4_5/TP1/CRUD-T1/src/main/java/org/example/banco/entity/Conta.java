package org.example.banco.entity;

import jakarta.persistence.*;
import org.example.banco.valueobjects.NomeTitular;
import org.example.banco.valueobjects.Saldo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidade que representa uma conta bancária no sistema.
 * 
 * Esta classe utiliza Value Objects para garantir a integridade dos dados
 * e aplicar regras de negócio de forma consistente.
 * 
 * Invariantes:
 * - Toda conta deve ter um titular válido
 * - Todo saldo deve ser não-negativo
 * - Uma vez criada, a conta não pode ter seu ID alterado
 */
@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeTitular;

    @Column(nullable = false)
    private Double valorSaldo;

    /**
     * Construtor padrão requerido pelo JPA.
     * Não deve ser usado diretamente no código de negócio.
     */
    protected Conta() {
        // Construtor vazio para JPA
    }

    /**
     * Constrói uma nova conta com titular e saldo inicial.
     *
     * @param nomeTitular o nome do titular da conta
     * @param saldo o saldo inicial da conta
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public Conta(final NomeTitular nomeTitular, final Saldo saldo) {
        validarParametrosObrigatorios(nomeTitular, saldo);
        this.nomeTitular = nomeTitular.getValor();
        this.valorSaldo = saldo.getValorDouble();
    }

    /**
     * Factory method para criar uma nova conta.
     *
     * @param nomeTitular o nome do titular
     * @param saldo o saldo inicial
     * @return uma nova instância de Conta
     */
    public static Conta criar(final NomeTitular nomeTitular, final Saldo saldo) {
        return new Conta(nomeTitular, saldo);
    }

    /**
     * Factory method para criar uma nova conta com saldo zero.
     *
     * @param nomeTitular o nome do titular
     * @return uma nova instância de Conta com saldo zerado
     */
    public static Conta criarComSaldoZero(final NomeTitular nomeTitular) {
        return new Conta(nomeTitular, Saldo.zero());
    }

    private void validarParametrosObrigatorios(final NomeTitular nomeTitular, final Saldo saldo) {
        if (nomeTitular == null) {
            throw new IllegalArgumentException("Nome do titular não pode ser nulo");
        }
        if (saldo == null) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        }
    }

    /**
     * Command: deposita um valor na conta.
     *
     * @param valor o valor a ser depositado
     * @throws IllegalArgumentException se o valor for inválido
     */
    public void depositar(final BigDecimal valor) {
        final Saldo saldoAtual = getSaldo();
        final Saldo novoSaldo = saldoAtual.adicionar(valor);
        this.valorSaldo = novoSaldo.getValorDouble();
    }

    /**
     * Command: saca um valor da conta.
     *
     * @param valor o valor a ser sacado
     * @throws IllegalArgumentException se o valor for inválido ou saldo insuficiente
     */
    public void sacar(final BigDecimal valor) {
        final Saldo saldoAtual = getSaldo();
        final Saldo novoSaldo = saldoAtual.subtrair(valor);
        this.valorSaldo = novoSaldo.getValorDouble();
    }

    /**
     * Command: atualiza o saldo da conta diretamente.
     * Deve ser usado com cautela, preferir usar depositar() e sacar().
     *
     * @param novoSaldo o novo saldo da conta
     * @throws IllegalArgumentException se o saldo for inválido
     */
    public void atualizarSaldo(final Saldo novoSaldo) {
        if (novoSaldo == null) {
            throw new IllegalArgumentException("Novo saldo não pode ser nulo");
        }
        this.valorSaldo = novoSaldo.getValorDouble();
    }

    /**
     * Command: altera o nome do titular da conta.
     *
     * @param novoNome o novo nome do titular
     * @throws IllegalArgumentException se o nome for inválido
     */
    public void alterarNomeTitular(final NomeTitular novoNome) {
        if (novoNome == null) {
            throw new IllegalArgumentException("Novo nome do titular não pode ser nulo");
        }
        this.nomeTitular = novoNome.getValor();
    }

    /**
     * Query: verifica se a conta possui saldo suficiente para um determinado valor.
     *
     * @param valor o valor a verificar
     * @return true se o saldo for suficiente, false caso contrário
     */
    public boolean possuiSaldoSuficiente(final BigDecimal valor) {
        return getSaldo().isSuficientePara(valor);
    }

    /**
     * Query: verifica se a conta está com saldo zerado.
     *
     * @return true se o saldo for zero, false caso contrário
     */
    public boolean isSaldoZerado() {
        return getSaldo().isZerado();
    }

    // Queries (Getters)

    /**
     * Query: retorna o ID da conta.
     *
     * @return o ID da conta
     */
    public Long getId() {
        return id;
    }

    /**
     * Query: retorna o nome do titular como Value Object.
     *
     * @return o nome do titular
     */
    public NomeTitular getNomeTitular() {
        return NomeTitular.de(nomeTitular);
    }

    /**
     * Query: retorna o saldo como Value Object.
     *
     * @return o saldo da conta
     */
    public Saldo getSaldo() {
        return Saldo.de(valorSaldo);
    }

    /**
     * Query: retorna o nome do titular como String (para compatibilidade).
     *
     * @return o nome do titular
     */
    public String getNomeTitularString() {
        return nomeTitular;
    }

    /**
     * Query: retorna o saldo como Double (para compatibilidade com banco de dados).
     *
     * @return o valor do saldo
     */
    public Double getSaldoDouble() {
        return valorSaldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(id, conta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Conta[ID=%d, Titular=%s, Saldo=%s]",
            id, nomeTitular, getSaldo());
    }
}
