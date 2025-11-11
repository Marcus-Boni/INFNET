package org.example.banco.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para a classe Saldo.
 * 
 * Cobertura de testes:
 * - Criação de saldo com diferentes tipos de entrada
 * - Operações de adição e subtração
 * - Validações de valores negativos
 * - Imutabilidade garantida
 * - Análise de limites e partições equivalentes
 */
@DisplayName("Saldo - Testes Unitários")
class SaldoTest {

    @Nested
    @DisplayName("Criação de Saldo")
    class CriacaoSaldo {

        @Test
        @DisplayName("Deve criar saldo a partir de BigDecimal válido")
        void deveCriarSaldoDeBigDecimal() {
            // Arrange & Act
            Saldo saldo = Saldo.de(new BigDecimal("100.50"));

            // Assert
            assertThat(saldo.getValor()).isEqualByComparingTo("100.50");
        }

        @Test
        @DisplayName("Deve criar saldo a partir de Double válido")
        void deveCriarSaldoDeDouble() {
            // Arrange & Act
            Saldo saldo = Saldo.de(250.75);

            // Assert
            assertThat(saldo.getValorDouble()).isEqualTo(250.75);
        }

        @Test
        @DisplayName("Deve criar saldo zero usando factory method")
        void deveCriarSaldoZero() {
            // Act
            Saldo saldo = Saldo.zero();

            // Assert
            assertThat(saldo.getValor()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(saldo.isZerado()).isTrue();
        }

        @Test
        @DisplayName("Deve arredondar para 2 casas decimais")
        void deveArredondarParaDuasCasasDecimais() {
            // Arrange & Act
            Saldo saldo = Saldo.de(new BigDecimal("100.999"));

            // Assert
            assertThat(saldo.getValor()).isEqualByComparingTo("101.00");
        }

        @Test
        @DisplayName("Deve aceitar saldo zero")
        void deveAceitarSaldoZero() {
            // Arrange & Act
            Saldo saldo = Saldo.de(BigDecimal.ZERO);

            // Assert
            assertThat(saldo.getValor()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("Validações de Criação")
    class ValidacoesCriacao {

        @Test
        @DisplayName("Deve lançar exceção quando BigDecimal é nulo")
        void deveLancarExcecaoQuandoBigDecimalNulo() {
            // Act & Assert
            assertThatThrownBy(() -> Saldo.de((BigDecimal) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Double é nulo")
        void deveLancarExcecaoQuandoDoubleNulo() {
            // Act & Assert
            assertThatThrownBy(() -> Saldo.de((Double) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção quando saldo é negativo")
        void deveLancarExcecaoQuandoSaldoNegativo() {
            // Act & Assert
            assertThatThrownBy(() -> Saldo.de(new BigDecimal("-10.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser negativo");
        }
    }

    @Nested
    @DisplayName("Operação de Adição")
    class OperacaoAdicao {

        @Test
        @DisplayName("Deve adicionar valor positivo ao saldo")
        void deveAdicionarValorPositivo() {
            // Arrange
            Saldo saldoInicial = Saldo.de(new BigDecimal("100.00"));

            // Act
            Saldo novoSaldo = saldoInicial.adicionar(new BigDecimal("50.00"));

            // Assert
            assertThat(novoSaldo.getValor()).isEqualByComparingTo("150.00");
        }

        @Test
        @DisplayName("Deve garantir imutabilidade ao adicionar")
        void deveGarantirImutabilidadeAoAdicionar() {
            // Arrange
            Saldo saldoInicial = Saldo.de(new BigDecimal("100.00"));

            // Act
            Saldo novoSaldo = saldoInicial.adicionar(new BigDecimal("50.00"));

            // Assert
            assertThat(saldoInicial.getValor()).isEqualByComparingTo("100.00");
            assertThat(novoSaldo.getValor()).isEqualByComparingTo("150.00");
        }

        @Test
        @DisplayName("Deve adicionar zero sem alterar o valor")
        void deveAdicionarZero() {
            // Arrange
            Saldo saldoInicial = Saldo.de(new BigDecimal("100.00"));

            // Act
            Saldo novoSaldo = saldoInicial.adicionar(BigDecimal.ZERO);

            // Assert
            assertThat(novoSaldo.getValor()).isEqualByComparingTo("100.00");
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar valor nulo")
        void deveLancarExcecaoAoAdicionarNull() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThatThrownBy(() -> saldo.adicionar(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar valor negativo")
        void deveLancarExcecaoAoAdicionarNegativo() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThatThrownBy(() -> saldo.adicionar(new BigDecimal("-10.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser negativo");
        }
    }

    @Nested
    @DisplayName("Operação de Subtração")
    class OperacaoSubtracao {

        @Test
        @DisplayName("Deve subtrair valor quando saldo for suficiente")
        void deveSubtrairQuandoSaldoSuficiente() {
            // Arrange
            Saldo saldoInicial = Saldo.de(new BigDecimal("100.00"));

            // Act
            Saldo novoSaldo = saldoInicial.subtrair(new BigDecimal("30.00"));

            // Assert
            assertThat(novoSaldo.getValor()).isEqualByComparingTo("70.00");
        }

        @Test
        @DisplayName("Deve garantir imutabilidade ao subtrair")
        void deveGarantirImutabilidadeAoSubtrair() {
            // Arrange
            Saldo saldoInicial = Saldo.de(new BigDecimal("100.00"));

            // Act
            Saldo novoSaldo = saldoInicial.subtrair(new BigDecimal("30.00"));

            // Assert
            assertThat(saldoInicial.getValor()).isEqualByComparingTo("100.00");
            assertThat(novoSaldo.getValor()).isEqualByComparingTo("70.00");
        }

        @Test
        @DisplayName("Deve permitir subtrair até zerar o saldo")
        void devePermitirSubtrairAteZero() {
            // Arrange
            Saldo saldoInicial = Saldo.de(new BigDecimal("100.00"));

            // Act
            Saldo novoSaldo = saldoInicial.subtrair(new BigDecimal("100.00"));

            // Assert
            assertThat(novoSaldo.getValor()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(novoSaldo.isZerado()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando saldo insuficiente")
        void deveLancarExcecaoQuandoSaldoInsuficiente() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("50.00"));

            // Act & Assert
            assertThatThrownBy(() -> saldo.subtrair(new BigDecimal("100.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo insuficiente");
        }

        @Test
        @DisplayName("Deve lançar exceção ao subtrair valor nulo")
        void deveLancarExcecaoAoSubtrairNull() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThatThrownBy(() -> saldo.subtrair(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção ao subtrair valor negativo")
        void deveLancarExcecaoAoSubtrairNegativo() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThatThrownBy(() -> saldo.subtrair(new BigDecimal("-10.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser negativo");
        }
    }

    @Nested
    @DisplayName("Queries - Consultas")
    class Queries {

        @Test
        @DisplayName("isSuficientePara deve retornar true quando saldo é suficiente")
        void isSuficienteParaDeveRetornarTrueQuandoSuficiente() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThat(saldo.isSuficientePara(new BigDecimal("50.00"))).isTrue();
            assertThat(saldo.isSuficientePara(new BigDecimal("100.00"))).isTrue();
        }

        @Test
        @DisplayName("isSuficientePara deve retornar false quando saldo é insuficiente")
        void isSuficienteParaDeveRetornarFalseQuandoInsuficiente() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThat(saldo.isSuficientePara(new BigDecimal("100.01"))).isFalse();
        }

        @Test
        @DisplayName("isSuficientePara deve retornar false quando valor é nulo")
        void isSuficienteParaDeveRetornarFalseQuandoNulo() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("100.00"));

            // Act & Assert
            assertThat(saldo.isSuficientePara(null)).isFalse();
        }

        @Test
        @DisplayName("isZerado deve retornar true para saldo zero")
        void isZeradoDeveRetornarTrueParaSaldoZero() {
            // Arrange
            Saldo saldo = Saldo.zero();

            // Act & Assert
            assertThat(saldo.isZerado()).isTrue();
        }

        @Test
        @DisplayName("isZerado deve retornar false para saldo positivo")
        void isZeradoDeveRetornarFalseParaSaldoPositivo() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("0.01"));

            // Act & Assert
            assertThat(saldo.isZerado()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equals e HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("Dois Saldos com mesmo valor devem ser iguais")
        void devemSerIguaisQuandoValoresIguais() {
            // Arrange
            Saldo saldo1 = Saldo.de(new BigDecimal("100.00"));
            Saldo saldo2 = Saldo.de(new BigDecimal("100.00"));

            // Assert
            assertThat(saldo1).isEqualTo(saldo2);
            assertThat(saldo1.hashCode()).isEqualTo(saldo2.hashCode());
        }

        @Test
        @DisplayName("Saldos com valores diferentes não devem ser iguais")
        void naoDevemSerIguaisQuandoValoresDiferentes() {
            // Arrange
            Saldo saldo1 = Saldo.de(new BigDecimal("100.00"));
            Saldo saldo2 = Saldo.de(new BigDecimal("100.01"));

            // Assert
            assertThat(saldo1).isNotEqualTo(saldo2);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringBehavior {

        @Test
        @DisplayName("toString deve formatar saldo como moeda")
        void toStringDeveFormatarComoMoeda() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("1234.56"));

            // Act & Assert
            assertThat(saldo.toString()).isEqualTo("R$ 1234.56");
        }

        @Test
        @DisplayName("toString deve formatar com duas casas decimais")
        void toStringDeveFormatarComDuasCasasDecimais() {
            // Arrange
            Saldo saldo = Saldo.de(new BigDecimal("10"));

            // Act & Assert
            assertThat(saldo.toString()).isEqualTo("R$ 10.00");
        }
    }
}
