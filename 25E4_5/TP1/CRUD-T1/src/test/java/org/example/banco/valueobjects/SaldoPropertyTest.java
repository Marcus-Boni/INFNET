package org.example.banco.valueobjects;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes baseados em propriedades para Saldo usando JQwik.
 * 
 * Estes testes geram automaticamente centenas de casos de teste
 * para verificar propriedades invariantes do sistema.
 */
class SaldoPropertyTest {

    /**
     * Propriedade: Saldo sempre deve ser não-negativo após criação.
     */
    @Property
    @Label("Saldo criado deve sempre ser não-negativo")
    void saldoCriadoSempreNaoNegativo(
        @ForAll @DoubleRange(min = 0.0, max = 1_000_000.0) double valor
    ) {
        // Act
        Saldo saldo = Saldo.de(valor);

        // Assert
        assertThat(saldo.getValor()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    /**
     * Propriedade: Adicionar um valor sempre aumenta ou mantém o saldo.
     */
    @Property
    @Label("Adicionar valor sempre aumenta ou mantém o saldo")
    void adicionarValorSempreAumentaSaldo(
        @ForAll @DoubleRange(min = 0.0, max = 10_000.0) double saldoInicial,
        @ForAll @DoubleRange(min = 0.0, max = 10_000.0) double valorAdicionar
    ) {
        // Arrange
        Saldo saldo = Saldo.de(saldoInicial);

        // Act
        Saldo novoSaldo = saldo.adicionar(BigDecimal.valueOf(valorAdicionar));

        // Assert
        assertThat(novoSaldo.getValor())
            .isGreaterThanOrEqualTo(saldo.getValor());
    }

    /**
     * Propriedade: Subtrair um valor válido sempre diminui ou mantém o saldo.
     */
    @Property
    @Label("Subtrair valor válido sempre diminui ou mantém o saldo")
    void subtrairValorValidoDiminuiSaldo(
        @ForAll @DoubleRange(min = 100.0, max = 10_000.0) double saldoInicial,
        @ForAll @DoubleRange(min = 0.0, max = 100.0) double valorSubtrair
    ) {
        // Arrange
        Saldo saldo = Saldo.de(saldoInicial);

        // Act
        Saldo novoSaldo = saldo.subtrair(BigDecimal.valueOf(valorSubtrair));

        // Assert
        assertThat(novoSaldo.getValor())
            .isLessThanOrEqualTo(saldo.getValor());
    }

    /**
     * Propriedade: Imutabilidade - operações não modificam o saldo original.
     */
    @Property
    @Label("Operações não modificam o saldo original (imutabilidade)")
    void operacoesNaoModificamSaldoOriginal(
        @ForAll @DoubleRange(min = 100.0, max = 10_000.0) double valor,
        @ForAll @DoubleRange(min = 0.0, max = 50.0) double operacao
    ) {
        // Arrange
        Saldo saldoOriginal = Saldo.de(valor);
        BigDecimal valorOriginal = saldoOriginal.getValor();

        // Act
        saldoOriginal.adicionar(BigDecimal.valueOf(operacao));
        saldoOriginal.subtrair(BigDecimal.valueOf(operacao));

        // Assert - saldo original não deve ter mudado
        assertThat(saldoOriginal.getValor()).isEqualByComparingTo(valorOriginal);
    }

    /**
     * Propriedade: Adicionar e depois subtrair o mesmo valor retorna ao saldo original.
     */
    @Property
    @Label("Adicionar e subtrair o mesmo valor retorna ao saldo original")
    void adicionarESubtrairMesmoValorRetornaOriginal(
        @ForAll @DoubleRange(min = 100.0, max = 10_000.0) double saldoInicial,
        @ForAll @DoubleRange(min = 0.0, max = 50.0) double valor
    ) {
        // Arrange
        Saldo saldoOriginal = Saldo.de(saldoInicial);

        // Act
        Saldo saldoDepoisAdicao = saldoOriginal.adicionar(BigDecimal.valueOf(valor));
        Saldo saldoFinal = saldoDepoisAdicao.subtrair(BigDecimal.valueOf(valor));

        // Assert
        assertThat(saldoFinal.getValor())
            .isEqualByComparingTo(saldoOriginal.getValor());
    }

    /**
     * Propriedade: Dois saldos com mesmo valor são iguais (equals).
     */
    @Property
    @Label("Saldos com mesmo valor são iguais")
    void saldosComMesmoValorSaoIguais(
        @ForAll @DoubleRange(min = 0.0, max = 10_000.0) double valor
    ) {
        // Act
        Saldo saldo1 = Saldo.de(valor);
        Saldo saldo2 = Saldo.de(valor);

        // Assert
        assertThat(saldo1).isEqualTo(saldo2);
        assertThat(saldo1.hashCode()).isEqualTo(saldo2.hashCode());
    }

    /**
     * Propriedade: isSuficientePara é consistente com comparação de valores.
     */
    @Property
    @Label("isSuficientePara é consistente com comparação")
    void isSuficienteParaConsistenteComComparacao(
        @ForAll @DoubleRange(min = 0.0, max = 10_000.0) double saldoValor,
        @ForAll @DoubleRange(min = 0.0, max = 10_000.0) double valorNecessario
    ) {
        // Arrange
        Saldo saldo = Saldo.de(saldoValor);

        // Act
        boolean suficiente = saldo.isSuficientePara(BigDecimal.valueOf(valorNecessario));
        boolean esperado = saldoValor >= valorNecessario;

        // Assert
        assertThat(suficiente).isEqualTo(esperado);
    }

    /**
     * Propriedade: Valores negativos sempre lançam exceção na criação.
     */
    @Property
    @Label("Valores negativos sempre lançam exceção")
    void valoresNegativosSempreLancamExcecao(
        @ForAll @DoubleRange(min = -10_000.0, max = -0.01) double valorNegativo
    ) {
        // Act & Assert
        assertThatThrownBy(() -> Saldo.de(valorNegativo))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("não pode ser negativo");
    }

    /**
     * Propriedade: Adicionar valores negativos sempre lança exceção.
     */
    @Property
    @Label("Adicionar valor negativo sempre lança exceção")
    void adicionarValorNegativoLancaExcecao(
        @ForAll @DoubleRange(min = 100.0, max = 1_000.0) double saldoInicial,
        @ForAll @DoubleRange(min = -1_000.0, max = -0.01) double valorNegativo
    ) {
        // Arrange
        Saldo saldo = Saldo.de(saldoInicial);

        // Act & Assert
        assertThatThrownBy(() -> saldo.adicionar(BigDecimal.valueOf(valorNegativo)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Propriedade: Subtrair mais que o saldo sempre lança exceção.
     */
    @Property
    @Label("Subtrair mais que o saldo sempre lança exceção")
    void subtrairMaisQueSaldoLancaExcecao(
        @ForAll @DoubleRange(min = 0.0, max = 100.0) double saldoInicial,
        @ForAll @DoubleRange(min = 100.01, max = 1_000.0) double valorSubtrair
    ) {
        // Arrange
        Saldo saldo = Saldo.de(saldoInicial);

        // Act & Assert
        assertThatThrownBy(() -> saldo.subtrair(BigDecimal.valueOf(valorSubtrair)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Saldo insuficiente");
    }

    /**
     * Propriedade: Saldo zero sempre retorna true para isZerado().
     */
    @Property
    @Label("Saldo zero sempre retorna true para isZerado")
    void saldoZeroRetornaTrueParaIsZerado() {
        // Act
        Saldo saldo = Saldo.zero();

        // Assert
        assertThat(saldo.isZerado()).isTrue();
    }

    /**
     * Propriedade: Saldo positivo sempre retorna false para isZerado().
     */
    @Property
    @Label("Saldo positivo sempre retorna false para isZerado")
    void saldoPositivoRetornaFalseParaIsZerado(
        @ForAll @DoubleRange(min = 0.01, max = 10_000.0) double valorPositivo
    ) {
        // Act
        Saldo saldo = Saldo.de(valorPositivo);

        // Assert
        assertThat(saldo.isZerado()).isFalse();
    }
}
